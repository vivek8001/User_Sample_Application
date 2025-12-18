# Keycloak Setup Guide for Task Management System

This guide explains how to configure Keycloak as the authentication and authorization provider for this Spring Boot application.

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Prerequisites](#prerequisites)
3. [Keycloak Installation & Setup](#keycloak-installation--setup)
4. [Spring Boot Configuration](#spring-boot-configuration)
5. [Testing the Integration](#testing-the-integration)
6. [Troubleshooting](#troubleshooting)

---

## Architecture Overview

```
User → Keycloak (Login) → JWT Token → Spring Boot API
                                      ↓
                              Validates JWT with Keycloak
                              Extracts roles from token
                              Authorizes based on roles
```

**Key Points:**
- Keycloak issues JWT tokens after user authentication
- Spring Boot validates tokens using Keycloak's public keys
- No passwords stored in Spring Boot application
- All user management happens in Keycloak
- Roles are extracted from JWT claims

---

## Prerequisites

- Docker (for running Keycloak) OR Java 17+ (for standalone Keycloak)
- PostgreSQL 12+ running on port 5432
- Maven 3.6+
- Java 17+

---

## Keycloak Installation & Setup

### Option 1: Using Docker (Recommended)

#### Step 1: Start Keycloak

```bash
docker run -d \
  --name keycloak \
  -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

**Important:** Keycloak runs on port **8180** to avoid conflict with Spring Boot on port 8080.

Wait 30-60 seconds for Keycloak to start, then access:
- Admin Console: http://localhost:8180/admin
- Username: `admin`
- Password: `admin`

---

### Option 2: Standalone Keycloak

1. Download Keycloak from: https://www.keycloak.org/downloads
2. Extract the archive
3. Set port to 8180:
   ```bash
   export KC_HTTP_PORT=8180
   ```
4. Start Keycloak:
   ```bash
   bin/kc.sh start-dev
   ```

---

## Keycloak Configuration

### Step 1: Create Realm

1. Login to Keycloak Admin Console: http://localhost:8180/admin
2. Click the dropdown at top-left (says "master")
3. Click **"Create Realm"**
4. **Realm name:** `taskmanagementsystem`
5. **Enabled:** ON
6. Click **Create**

---

### Step 2: Create Client (Application)

1. In the `taskmanagementsystem` realm, go to **Clients** (left sidebar)
2. Click **"Create client"**
3. **General Settings:**
   - **Client type:** OpenID Connect
   - **Client ID:** `spring-boot-app`
   - Click **Next**

4. **Capability config:**
   - **Client authentication:** OFF (for public clients) or ON (for confidential clients with client secret)
   - **Authorization:** OFF
   - **Authentication flow:**
     - ✅ Standard flow
     - ✅ Direct access grants (for username/password login)
     - ✅ Implicit flow (optional, for browser-based apps)
   - Click **Next**

5. **Login settings:**
   - **Root URL:** `http://localhost:8080`
   - **Home URL:** `http://localhost:8080`
   - **Valid redirect URIs:** `http://localhost:8080/*`
   - **Valid post logout redirect URIs:** `http://localhost:8080/*`
   - **Web origins:** `http://localhost:8080` (enables CORS)
   - Click **Save**

---

### Step 3: Create Roles

1. Go to **Realm roles** (left sidebar)
2. Click **"Create role"**
3. Create the following roles:

   **Role 1: USER**
   - **Role name:** `USER`
   - **Description:** Regular user role
   - Click **Save**

   **Role 2: ADMIN**
   - **Role name:** `ADMIN`
   - **Description:** Administrator role
   - Click **Save**

   **Role 3: MODERATOR** (optional)
   - **Role name:** `MODERATOR`
   - **Description:** Moderator role
   - Click **Save**

---

### Step 4: Create Users

#### Create Admin User

1. Go to **Users** (left sidebar)
2. Click **"Add user"**
3. **User details:**
   - **Username:** `admin`
   - **Email:** `admin@example.com`
   - **First name:** `Admin`
   - **Last name:** `User`
   - **Email verified:** ON
   - **Enabled:** ON
   - Click **Create**

4. **Set Password:**
   - Go to **Credentials** tab
   - Click **"Set password"**
   - **Password:** `admin123` (or your choice)
   - **Password confirmation:** `admin123`
   - **Temporary:** OFF (turn this off so user doesn't need to reset)
   - Click **Save**
   - Confirm in the popup

5. **Assign Roles:**
   - Go to **Role mappings** tab
   - Click **"Assign role"**
   - Select **ADMIN** role
   - Click **Assign**

#### Create Regular User

1. Go to **Users** → **"Add user"**
2. **User details:**
   - **Username:** `testuser`
   - **Email:** `testuser@example.com`
   - **First name:** `Test`
   - **Last name:** `User`
   - **Email verified:** ON
   - **Enabled:** ON
   - Click **Create**

3. **Set Password:**
   - Go to **Credentials** tab
   - Set password: `password123`
   - **Temporary:** OFF
   - Click **Save**

4. **Assign Roles:**
   - Go to **Role mappings** tab
   - Assign **USER** role

---

## Spring Boot Configuration

### Step 1: Verify application.properties

The application is already configured with these settings:

```properties
# Keycloak OAuth2 Resource Server Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8180/realms/taskmanagementsystem
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/certs
```

**If your Keycloak is on a different host/port, update these URLs.**

---

### Step 2: Build the Application

```bash
# Navigate to project directory
cd c:\Users\HariharanB\Downloads\User_Sample_Application-main

# Clean and install dependencies
mvn clean install
```

---

### Step 3: Ensure PostgreSQL is Running

Make sure PostgreSQL is running with:
- Host: `localhost`
- Port: `5432`
- Database: `userManagement`
- Username: `postgres`
- Password: `root123`

Create the database if it doesn't exist:
```sql
CREATE DATABASE userManagement;
```

---

### Step 4: Start Spring Boot Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

---

## Testing the Integration

### Test 1: Verify Keycloak Configuration

Get authentication endpoints:
```bash
curl http://localhost:8080/api/auth/info
```

**Expected Response:**
```json
{
  "issuer": "http://localhost:8180/realms/taskmanagementsystem",
  "token_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/token",
  "authorization_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/auth",
  "userinfo_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/userinfo",
  "logout_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/logout",
  "jwks_uri": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/certs"
}
```

---

### Test 2: Get JWT Token from Keycloak

#### For Regular User:
```bash
curl -X POST 'http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'client_id=spring-boot-app' \
  -d 'username=testuser' \
  -d 'password=password123' \
  -d 'grant_type=password'
```

#### For Admin User:
```bash
curl -X POST 'http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'client_id=spring-boot-app' \
  -d 'username=admin' \
  -d 'password=admin123' \
  -d 'grant_type=password'
```

**Expected Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "scope": "profile email"
}
```

**Copy the `access_token` value** - you'll need it for the next tests.

---

### Test 3: Access Protected Endpoints

#### Test Public Endpoint (No Token Required):
```bash
curl http://localhost:8080/api/user/home
```

**Expected:** `Welcome Home`

---

#### Test Authenticated Endpoint (Token Required):
Replace `YOUR_TOKEN_HERE` with the actual access_token from Test 2.

```bash
curl -X GET 'http://localhost:8080/api/tasks' \
  -H 'Authorization: Bearer YOUR_TOKEN_HERE'
```

**Expected:** List of tasks (or empty array if no tasks exist)

---

#### Test Role-Based Authorization (ADMIN only):
```bash
# This should work with admin token
curl -X GET 'http://localhost:8080/api/user/all' \
  -H 'Authorization: Bearer ADMIN_TOKEN_HERE'

# This should FAIL (403 Forbidden) with regular user token
curl -X GET 'http://localhost:8080/api/user/all' \
  -H 'Authorization: Bearer USER_TOKEN_HERE'
```

**Expected:**
- Admin token: Returns all users
- User token: 403 Forbidden

---

### Test 4: Test Without Token (Should Fail)

```bash
curl -X GET 'http://localhost:8080/api/tasks'
```

**Expected Response:**
```json
{
  "error": "Unauthorized",
  "status": 401,
  "message": "Full authentication is required to access this resource"
}
```

This confirms that authentication is working! ✅

---

## Troubleshooting

### Problem 1: 401 Unauthorized Error

**Symptoms:**
```json
{
  "error": "Unauthorized",
  "status": 401
}
```

**Possible Causes & Solutions:**

1. **No token provided**
   - Ensure you include `Authorization: Bearer <token>` header

2. **Token expired**
   - Keycloak tokens expire (usually 5-30 minutes)
   - Get a new token from Keycloak

3. **Wrong issuer-uri**
   - Verify `spring.security.oauth2.resourceserver.jwt.issuer-uri` matches your Keycloak realm URL
   - Check Keycloak is running on correct port (8180)

4. **Keycloak not reachable**
   - Test: `curl http://localhost:8180/realms/taskmanagementsystem`
   - Should return realm information

5. **Wrong realm or client**
   - Verify realm name is `taskmanagementsystem`
   - Verify client ID is `spring-boot-app`

---

### Problem 2: 403 Forbidden Error

**Symptoms:**
```json
{
  "error": "Forbidden",
  "status": 403,
  "message": "Access Denied"
}
```

**Possible Causes & Solutions:**

1. **User doesn't have required role**
   - Check user has correct role assigned in Keycloak
   - Go to Users → [username] → Role mappings
   - Ensure role is assigned (e.g., ADMIN, USER)

2. **Role not in JWT token**
   - Decode your JWT at https://jwt.io
   - Check `realm_access.roles` contains the role
   - If missing, role wasn't assigned in Keycloak

3. **Wrong role name**
   - Controller expects `@PreAuthorize("hasRole('ADMIN')")`
   - Keycloak role must be named exactly `ADMIN` (case-sensitive)

---

### Problem 3: CORS Error (From Browser)

**Symptoms:**
```
Access to XMLHttpRequest has been blocked by CORS policy
```

**Solutions:**

1. **Add your frontend origin to Keycloak Client:**
   - Go to Clients → `spring-boot-app` → Settings
   - Add your frontend URL to **Web origins** (e.g., `http://localhost:3000`)

2. **Update Spring Boot CORS config:**
   - Edit `SecurityConfig.java`
   - Add your frontend URL to `allowedOrigins`:
     ```java
     configuration.setAllowedOrigins(Arrays.asList(
         "http://localhost:3000",  // Your frontend URL
         "http://localhost:4200"
     ));
     ```

---

### Problem 4: Can't Get Token from Keycloak

**Error:**
```json
{
  "error": "invalid_grant",
  "error_description": "Invalid user credentials"
}
```

**Solutions:**

1. **Wrong username/password**
   - Verify credentials in Keycloak Users section
   - Check "Temporary password" is OFF

2. **User disabled**
   - Go to Users → [username]
   - Ensure **Enabled** toggle is ON

3. **Wrong client_id**
   - Verify you're using `client_id=spring-boot-app`

4. **Direct access grants disabled**
   - Go to Clients → `spring-boot-app` → Settings
   - Ensure **Direct access grants** is enabled

---

### Problem 5: Application Can't Connect to Keycloak

**Error in Spring Boot logs:**
```
Unable to obtain JWK Set from http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/certs
```

**Solutions:**

1. **Keycloak not running**
   - Check: `docker ps` (if using Docker)
   - Start: `docker start keycloak`

2. **Wrong port**
   - Verify Keycloak is on port 8180
   - Test: `curl http://localhost:8180`

3. **Wrong realm name**
   - Check realm exists: http://localhost:8180/realms/taskmanagementsystem
   - Verify spelling in `application.properties`

---

## Role-Based Access Control (RBAC)

### Current Configuration:

| Endpoint | Method | Required Role | Description |
|----------|--------|---------------|-------------|
| `/api/auth/**` | ALL | None (Public) | Authentication info |
| `/api/user/home` | GET | None (Public) | Welcome message |
| `/api/user/all` | GET | ADMIN | Get all users |
| `/api/user/{id}` | GET | USER or ADMIN | Get user by ID |
| `/api/user` | POST | Authenticated | Create user |
| `/api/user` | PUT | Authenticated | Update user |
| `/api/user/{id}` | DELETE | Authenticated | Delete user |
| `/api/tasks/**` | ALL | Authenticated | Task management |
| `/api/projects/**` | ALL | Authenticated | Project management |

### Adding More Roles:

1. Create role in Keycloak (Realm roles)
2. Assign to users
3. Use in controllers:
   ```java
   @PreAuthorize("hasRole('YOUR_ROLE')")
   ```

---

## Production Checklist

Before deploying to production:

- [ ] Change Keycloak admin password
- [ ] Use external database for Keycloak (not H2)
- [ ] Enable HTTPS for Keycloak
- [ ] Enable HTTPS for Spring Boot
- [ ] Use confidential client (with client secret)
- [ ] Set appropriate token expiration times
- [ ] Configure refresh token rotation
- [ ] Set up Keycloak clustering for high availability
- [ ] Enable email verification for users
- [ ] Configure password policies
- [ ] Set up monitoring and logging
- [ ] Back up Keycloak database
- [ ] Review CORS origins (restrict to production domains)
- [ ] Disable DEBUG logging in production

---

## Useful Keycloak URLs

- Admin Console: http://localhost:8180/admin
- Realm Info: http://localhost:8180/realms/taskmanagementsystem
- Token Endpoint: http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/token
- JWK Set (Public Keys): http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/certs
- User Info: http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/userinfo

---

## Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [JWT.io - Decode JWT tokens](https://jwt.io)
- [Keycloak Docker Image](https://quay.io/repository/keycloak/keycloak)

---

## Support

If you encounter issues:

1. Check application logs: `mvn spring-boot:run` output
2. Check Keycloak logs: `docker logs keycloak`
3. Enable DEBUG logging in `application.properties`:
   ```properties
   logging.level.org.springframework.security=DEBUG
   ```
4. Decode your JWT token at https://jwt.io to verify claims

---

**Last Updated:** 2024
**Author:** Task Management System Team
