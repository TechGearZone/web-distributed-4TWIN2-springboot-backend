#!/bin/bash

# Wait for Keycloak to be ready
echo "Waiting for Keycloak to be ready..."
while ! curl -s http://localhost:8080 > /dev/null; do
    sleep 5
done

# Login to get admin token
echo "Logging in to Keycloak..."
ADMIN_TOKEN=$(curl -X POST http://localhost:8080/realms/master/protocol/openid-connect/token \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=admin" \
    -d "password=admin" \
    -d "grant_type=password" \
    -d "client_id=admin-cli" | jq -r '.access_token')

# Create realm
echo "Creating techgear realm..."
curl -X POST http://localhost:8080/admin/realms \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
    "realm": "techgear",
    "enabled": true,
    "displayName": "TechGear E-commerce"
}'

# Create client
echo "Creating techgear-client..."
curl -X POST http://localhost:8080/admin/realms/techgear/clients \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
    "clientId": "techgear-client",
    "enabled": true,
    "clientAuthenticatorType": "client-secret",
    "secret": "SjsmyTNP8TX3QkYNf3woG2Gt",
    "redirectUris": ["*"],
    "webOrigins": ["*"],
    "publicClient": false,
    "protocol": "openid-connect",
    "directAccessGrantsEnabled": true
}'

# Create test user
echo "Creating admin user..."
curl -X POST http://localhost:8080/admin/realms/techgear/users \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
    "username": "admin",
    "enabled": true,
    "credentials": [{
        "type": "password",
        "value": "admin123",
        "temporary": false
    }],
    "realmRoles": ["admin"]
}'

echo "Keycloak setup completed!" 