package com.app.controller;

import com.app.dao.AppDAO;
import com.app.model.User;
import com.app.model.Material;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.UUID;

// **CRITICAL:** Enables multipart/form-data processing for file uploads
@WebServlet("/MaterialController")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class MaterialController extends HttpServlet {
    private AppDAO appDAO;
    // Set a consistent, accessible path within the Tomcat container
    private static final String UPLOAD_DIRECTORY = "/opt/tomcat/webapps/data/materials/"; 
    // This directory MUST be created/writable in the Docker/Render environment

    public void init() throws ServletException {
        appDAO = new AppDAO();
        // **Create the upload directory on init**
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if (command == null) {
            command = "LIST";
        }

        try {
            switch (command) {
                case "LIST":
                    listMaterials(request, response);
                    break;
                case "LOAD":
                    // Generally used to load an EDIT form, but here, we just load the UPLOAD form
                    request.getRequestDispatcher("/WEB-INF/views/upload-form.jsp").forward(request, response);
                    break;
                case "DOWNLOAD":
                    downloadMaterial(request, response);
                    break;
                case "DELETE":
                    deleteMaterial(request, response);
                    break;
                default:
                    listMaterials(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if (command == null) {
            command = "UPLOAD";
        }

        try {
            switch (command) {
                case "UPLOAD":
                    uploadMaterial(request, response);
                    break;
                default:
                    listMaterials(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    // --- Core Methods ---

    private void listMaterials(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // **Data Isolation:** Get only the materials owned by the current user
        List<Material> materials = appDAO.getAllMaterialsByUserId(user.getId());
        
        request.setAttribute("MATERIALS_LIST", materials);
        request.getRequestDispatcher("/WEB-INF/views/list-materials.jsp").forward(request, response);
    }

    private void uploadMaterial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 1. Get file part and metadata
        Part filePart = request.getPart("file");
        String title = request.getParameter("title");
        String course = request.getParameter("course");
        String description = request.getParameter("description");

        String originalFileName = filePart.getSubmittedFileName();
        // **Security/Unique Naming:** Generate a unique, secure file name
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        
        // 2. Save file to disk
        String filePath = UPLOAD_DIRECTORY + storedFileName;
        filePart.write(filePath); // Writes the file to the path on the server

        // 3. Save metadata to database
        Material material = new Material();
        material.setUserId(user.getId());
        material.setTitle(title);
        material.setCourse(course);
        material.setDescription(description);
        material.setFileName(originalFileName);
        material.setStoredName(storedFileName);
        
        appDAO.addMaterial(material);

        // Redirect back to the list
        response.sendRedirect("MaterialController?command=LIST");
    }

    private void downloadMaterial(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        
        // **Security Check:** Retrieve material only if the user owns it
        Material material = appDAO.getMaterialByIdAndUser(materialId, user.getId());

        if (material != null) {
            String fullPath = UPLOAD_DIRECTORY + material.getStoredName();
            File downloadFile = new File(fullPath);

            if (downloadFile.exists()) {
                // Set MIME type
                String mimeType = getServletContext().getMimeType(material.getFileName());
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                
                // Set content type and attachment header
                response.setContentType(mimeType);
                response.setContentLength((int) downloadFile.length());
                
                // Encode the file name to handle spaces/special characters
                String encodedFileName = URLEncoder.encode(material.getFileName(), "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

                // Stream the file content to the response
                try (FileInputStream inStream = new FileInputStream(downloadFile);
                     OutputStream outStream = response.getOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;

                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.getWriter().println("File not found on server.");
            }
        } else {
            // Security failure or material does not exist
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found or access denied.");
        }
    }

    private void deleteMaterial(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int materialId = Integer.parseInt(request.getParameter("materialId"));

        // 1. Get material details before deleting the DB record (for file deletion)
        Material material = appDAO.getMaterialByIdAndUser(materialId, user.getId());
        boolean dbDeleted = false;

        if (material != null) {
            // 2. Delete the DB record (Data Isolation check happens here)
            dbDeleted = appDAO.deleteMaterial(materialId, user.getId());

            if (dbDeleted) {
                // 3. Delete the actual file from disk
                String fullPath = UPLOAD_DIRECTORY + material.getStoredName();
                File fileToDelete = new File(fullPath);
                
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
                // No need to fail if file delete fails, as DB record is gone.
            }
        }
        
        response.sendRedirect("MaterialController?command=LIST");
    }
}