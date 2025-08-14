# Board System REST API

A modular REST API server built with Spring Boot, MyBatis, and MySQL featuring a board system with package-based architecture.

## Architecture

### Package Structure
- **user package**: User management (controller, service, repository, dto)
- **board package**: Board management (controller, service, repository, dto)  
- **common package**: Shared components (exception handling)

### Layer Structure
- **Controller Layer**: REST endpoints for handling HTTP requests
- **Service Layer**: Business logic and data validation (interface + implementation)
- **Repository Layer**: Data access using MyBatis with XML mappers
- **DTO Layer**: Data Transfer Objects for request/response

## Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE board_system;
```

2. Run the schema script located in `src/main/resources/schema.sql`

3. Update database credentials in `application.properties`

## API Endpoints

### Users API (`/api/users`)

#### Create User
- **POST** `/api/users`
- **Body**:
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Get User by ID
- **GET** `/api/users/{id}`

#### Get All Users
- **GET** `/api/users`

#### Get User by Username
- **GET** `/api/users/username/{username}`

#### Update User
- **PUT** `/api/users/{id}`
- **Body**: Same as Create User

#### Delete User
- **DELETE** `/api/users/{id}`

### Boards API (`/api/boards`)

#### Create Board
- **POST** `/api/boards`
- **Body**:
```json
{
  "title": "General Discussion",
  "description": "A place for general discussions",
  "userId": 1
}
```

#### Get Board by ID
- **GET** `/api/boards/{id}`

#### Get All Boards
- **GET** `/api/boards`

#### Get Boards by User ID
- **GET** `/api/boards/user/{userId}`

#### Update Board
- **PUT** `/api/boards/{id}`
- **Body**: Same as Create Board

#### Delete Board
- **DELETE** `/api/boards/{id}?userId={userId}`


## Response Format

### Success Response
```json
{
  "id": 1,
  "title": "Example",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

### Error Response
```json
{
  "status": 400,
  "message": "Error message",
  "timestamp": "2024-01-01T12:00:00",
  "validationErrors": {
    "field": "error message"
  }
}
```

## Validation Rules

### User
- Username: 3-50 characters, required, unique
- Email: Valid email format, required, unique
- Password: Minimum 6 characters, required

### Board
- Title: Maximum 100 characters, required
- Description: Maximum 500 characters, optional
- User ID: Required, must exist


## Running the Application

1. Ensure MySQL is running
2. Create the database and run schema.sql
3. Update application.properties with your database credentials
4. Run: `mvn spring-boot:run`
5. API will be available at `http://localhost:8080`

## Features

- Full CRUD operations for Users and Boards
- Input validation with detailed error messages
- Global exception handling
- Proper HTTP status codes
- Clean architecture with separation of concerns
- MyBatis with XML mappers for database operations
- Foreign key relationships and cascading deletes