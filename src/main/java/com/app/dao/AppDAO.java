public AppDAO() {
        try {
            // **CRITICAL for Render Deployment:** Get DB config from Environment Variable
            String dbUrl = System.getenv("DATABASE_URL");

            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DATABASE_URL environment variable is not set. Cannot connect to database.");
            }

            Class.forName("org.postgresql.Driver");

            // 1. Extract components from URL
            // Render format: postgres://user:password@host:port/database
            URI dbUri = new URI(dbUrl);
            
            // Extract User/Password
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            
            String host = dbUri.getHost();
            
            // 2. FIX: Handle invalid port (-1) returned by URI parsing by defaulting to 5432
            int port = dbUri.getPort();
            if (port == -1) {
                // Default PostgreSQL port
                port = 5432; 
            }
            
            String path = dbUri.getPath();

            // 3. Construct the correct JDBC connection string
            // Fixes the issue where ':-1' was inserted into the JDBC URL
            String dbConnString = "jdbc:postgresql://" + host + ':' + port + path;
            
            // 4. Establish connection
            this.connection = DriverManager.getConnection(dbConnString, username, password);
            System.out.println("Database connection established successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed during startup.", e);
        }
    }
