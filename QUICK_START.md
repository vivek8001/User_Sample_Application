# Quick Start Guide - Keycloak Integration

## 🚀 Quick Setup (5 Minutes)

### Step 1: Start Keycloak (1 min)
```bash
docker run -d --name keycloak -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

Wait 30 seconds, then open: http://localhost:8180/admin (admin/admin)

---

### Step 2: Create Realm (1 min)
1. Click dropdown at top-left → **Create Realm**
2. Name: `taskmanagementsystem`
3. Click **Create**

---

### Step 3: Create Client (1 min)
1. Go to **Clients** → **Create client**
2. Client ID: `spring-boot-app`
3. Click **Next** → **Next**
4. Valid redirect URIs: `http://localhost:8080/*`
5. Web origins: `http://localhost:8080`
6. Click **Save**

---

### Step 4: Create Roles (30 sec)
1. Go to **Realm roles**
2. Create role: `USER`
3. Create role: `ADMIN`

---

### Step 5: Create User (1 min)
1. Go to **Users** → **Add user**
2. Username: `testuser`, Email: `test@example.com`
3. Click **Create**
4. **Credentials** tab → Set password: `password123` (Temporary: OFF)
5. **Role mappings** tab → Assign role: `USER`

Repeat for admin user:
- Username: `admin`, Password: `admin123`, Role: `ADMIN`

---

### Step 6: Start PostgreSQL
```bash
# Ensure PostgreSQL is running with:
# - Database: userManagement
# - User: postgres
# - Password: root123
```

---

### Step 7: Start Spring Boot (1 min)
```bash
cd c:\Users\HariharanB\Downloads\User_Sample_Application-main
mvn clean install
mvn spring-boot:run
```

---

## ✅ Test It!

### 1. Get Token
```bash
curl -X POST 'http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'client_id=spring-boot-app' \
  -d 'username=testuser' \
  -d 'password=password123' \
  -d 'grant_type=password'
```

Copy the `access_token` from the response.

---

### 2. Test API (Replace YOUR_TOKEN)
```bash
# Public endpoint (no token needed)
curl http://localhost:8080/api/user/home

# Protected endpoint (requires token)
curl -X GET 'http://localhost:8080/api/tasks' \
  -H 'Authorization: Bearer YOUR_TOKEN'

# Admin-only endpoint (requires ADMIN role)
curl -X GET 'http://localhost:8080/api/user/all' \
  -H 'Authorization: Bearer ADMIN_TOKEN'
```

---

## 🐛 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| 401 Unauthorized | Token expired or missing. Get new token. |
| 403 Forbidden | User doesn't have required role. Check Keycloak role mappings. |
| Can't connect to Keycloak | Verify Keycloak is running on port 8180 |
| Invalid credentials | Check username/password in Keycloak, ensure "Temporary" is OFF |

---

## 📝 Important URLs

- Keycloak Admin: http://localhost:8180/admin
- Spring Boot API: http://localhost:8080
- Auth Info: http://localhost:8080/api/auth/info
- Login Help: http://localhost:8080/api/auth/how-to-login

---

## 🔑 Default Credentials

**Keycloak Admin:**
- Username: `admin`
- Password: `admin`

**Test Users:**
- User: `testuser` / `password123` (Role: USER)
- Admin: `admin` / `admin123` (Role: ADMIN)

**PostgreSQL:**
- User: `postgres`
- Password: `root123`
- Database: `userManagement`

---

## 📚 Full Documentation

See [KEYCLOAK_SETUP_GUIDE.md](KEYCLOAK_SETUP_GUIDE.md) for detailed setup instructions and troubleshooting.

---

**That's it! Your API is now secured with Keycloak!** 🎉
