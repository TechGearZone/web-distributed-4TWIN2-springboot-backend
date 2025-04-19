# Wait for Keycloak to be ready
Write-Host "Waiting for Keycloak to be ready..."
while ($true) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080" -Method GET -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            break
        }
    } catch {
        Start-Sleep -Seconds 5
    }
}

# Login to get admin token
Write-Host "Logging in to Keycloak..."
$tokenResponse = Invoke-RestMethod -Uri "http://localhost:8080/realms/master/protocol/openid-connect/token" `
    -Method Post `
    -Body @{
        username = "admin"
        password = "admin"
        grant_type = "password"
        client_id = "admin-cli"
    } `
    -ContentType "application/x-www-form-urlencoded"

$adminToken = $tokenResponse.access_token

# Create realm
Write-Host "Creating techgear realm..."
$realmBody = @{
    realm = "techgear"
    enabled = $true
    displayName = "TechGear E-commerce"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/admin/realms" `
    -Method Post `
    -Headers @{ Authorization = "Bearer $adminToken" } `
    -ContentType "application/json" `
    -Body $realmBody

# Create roles
Write-Host "Creating roles..."
# Create admin role
$adminRoleBody = @{
    name = "admin"
    description = "Administrator role"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/roles" `
    -Method Post `
    -Headers @{ Authorization = "Bearer $adminToken" } `
    -ContentType "application/json" `
    -Body $adminRoleBody

# Create user role
$userRoleBody = @{
    name = "user"
    description = "Regular user role"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/roles" `
    -Method Post `
    -Headers @{ Authorization = "Bearer $adminToken" } `
    -ContentType "application/json" `
    -Body $userRoleBody

# Create client
Write-Host "Creating techgear-client..."
$clientBody = @{
    clientId = "techgear-client"
    enabled = $true
    clientAuthenticatorType = "client-secret"
    secret = "SjsmyTNP8TX3QkYNf3woG2Gt"
    redirectUris = @("*")
    webOrigins = @("*")
    publicClient = $false
    protocol = "openid-connect"
    directAccessGrantsEnabled = $true
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/clients" `
    -Method Post `
    -Headers @{ Authorization = "Bearer $adminToken" } `
    -ContentType "application/json" `
    -Body $clientBody

# Create admin user
Write-Host "Creating admin user..."
$userBody = @{
    username = "admin"
    enabled = $true
    emailVerified = $true
    credentials = @(
        @{
            type = "password"
            value = "admin123"
            temporary = $false
        }
    )
} | ConvertTo-Json

$adminUser = Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/users" `
    -Method Post `
    -Headers @{ Authorization = "Bearer $adminToken" } `
    -ContentType "application/json" `
    -Body $userBody

# Get the admin user ID
$users = Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/users?username=admin" `
    -Method Get `
    -Headers @{ Authorization = "Bearer $adminToken" }
$adminUserId = $users[0].id

# Assign admin role to admin user
Write-Host "Assigning admin role to admin user..."
$adminRole = Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/roles/admin" `
    -Method Get `
    -Headers @{ Authorization = "Bearer $adminToken" }

$roleMapping = @(
    @{
        id = $adminRole.id
        name = $adminRole.name
    }
) | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/admin/realms/techgear/users/$adminUserId/role-mappings/realm" `
    -Method Post `
    -Headers @{ Authorization = "Bearer $adminToken" } `
    -ContentType "application/json" `
    -Body $roleMapping

Write-Host "Keycloak setup completed!" 