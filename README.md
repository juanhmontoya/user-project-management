# Projects & Users APIs

## Reference Documentation
This application will help in the creation and management of Projects and Users.
The service will provide REST APIs to create Users and add them to a given
Project.
List Users and Projects. Also create, update and delete Projects and users.
The implementation was done with a relational in-memory database H2 with Spring
Data JPA persistence framework.

The database has preloaded data that will be created at application startup for
the sake of testing and start playing around.

## Guides
To run the application execute the following command in the
command line interface:

````
mvn spring-boot:run
````
## User operations
### Create User
Will create a new user entity in the database.
If the name or email fields are missing it will return an error.
Also if the email is not a valid format it will return an error.

HttpMethod: POST

Path: `/api/v1/users`

Input:
- request body: user entity fields name and email.

#### Success

Request:
````
curl --location 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "James",
    "email": "jgosling@google.com"
}'
```` 
Response:

`status: 200 OK`
````
{
    "id": 25,
    "name": "James",
    "email": "jgosling@sun.com"
}
```` 

#### Error cases

Missing name field.

Request:
````
curl --location 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "robpike@google.com"
}'
```` 
Response:

`status: 400 Bad Request`
````
{
    "status": 400,
    "error": "Bad Request",
    "message": "field name is not valid",
    "timestamp": "2023-05-31T22:21:02.125"
}
````

Not valid email.

Request:
````
curl --location 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Rob",
    "email": "robpikegoogle.com"
}'
```` 
Response:

`status: 400 Bad Request`
````
{
    "status": 400,
    "error": "Bad Request",
    "message": "email format is not valid",
    "timestamp": "2023-05-31T22:20:01.346"
}
````

Already existing email.

Request:
````
curl --location 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Jake",
    "email": "robpike@google.com"
}'
```` 
Response:

`status: 409 Conflict`
````
{
    "timestamp": "2023-05-31T13:05:25.549",
    "message": "given email already exists",
    "error": "Conflict",
    "status": 409
}
````

### Find User by id
Returns the user information as well as the projects that is currently assigned.
Will return error if the user is not found.

HttpMethod: GET

Path: `/api/v1/users/{userId}`

Input:
- userId: user id to be found

#### Success

````
curl --location 'localhost:9091/api/v1/users/13'
```` 
Response:

`status: 200 OK`
````
{
    "id": 13,
    "name": "Bill",
    "email": "bkennedy@google.com",
    "projects": [
        {
            "id": 9,
            "name": "JEP-323",
            "description": "Local-variable syntax for lambda parameters"
        }
    ]
}
```` 

#### Error cases

````
curl --location 'localhost:9091/api/v1/users/900'
```` 
Response:

`status: 404 Not Found`
````
{
    "status": 404,
    "error": "Not Found",
    "message": "user with id=900 not found",
    "timestamp": "2023-05-31T22:40:36.624"
}
```` 

### Find User by name

HttpMethod: GET

Path: `/api/v1/users?name={username}`

Input:
- username: user name to be found

#### Success

````
curl --location 'localhost:9091/api/v1/users?name=Rob'
```` 
Response:

`status: 200 OK`
````
{
    "id": 1,
    "name": "Rob",
    "email": "robpike@google.com"
}
```` 

#### Error Cases

Request:
````
curl --location 'localhost:9091/api/v1/users?name=Jackie'
````

Response:

`status: 404 Not Found`
````
{
    "status": 404,
    "error": "Not Found",
    "message": "user with name=Jackie not found",
    "timestamp": "2023-05-31T22:42:58.486"
}
````

### Find User by email
````
curl --location 'localhost:9091/api/v1/users?email=avanhoff%40sun.com'
```` 
Response:

`status: 200 OK`
````
{
    "id": 8,
    "name": "Arthur",
    "email": "avanhoff@sun.com"
}
```` 
### Find All Users
Implementation for returning array of objects will be paginated.
So the values 'page' and 'size' can be provided as query params.
The default value for the page size is 10 and starting page is 0.
If there are no existing projects in the database will return an error.

HttpMethod: GET

Path: `/api/v1/users?page={pageNumber}&size={pageSize}`

Input:
- pageNumber: page index to retrieve (optional)
- pageSize: size of the elements returned in the page (optional)

#### Success
Request:
````
curl --location 'localhost:9091/api/v1/users?page=0&size=5'
```` 
Response:

`status: 200 OK`
````
{
    "users": [
        {
            "id": 1,
            "name": "Rob",
            "email": "robpike@google.com"
        },
        {
            "id": 2,
            "name": "James",
            "email": "jgosling@sun.com"
        },
        {
            "id": 3,
            "name": "Brian",
            "email": "bgoetz@oracle.com"
        },
        {
            "id": 4,
            "name": "Max",
            "email": "mbonfit@google.com"
        },
        {
            "id": 5,
            "name": "Jim",
            "email": "jbrown@sun.com"
        }
    ],
    "total-items": 24,
    "current-page": 0,
    "total-pages": 5
}
```` 
#### Error cases

Request:

````
curl --location 'localhost:9091/api/v1/projects'
```` 
Response:

`status: 404 Not Found`
````
{
    "message": "no projects loaded",
    "timestamp": "2023-05-31T23:01:05.533",
    "status": 404,
    "error": "Not Found"
}
```` 

### Update User
Will modify an existing user. Will return an error if the modified field is
the email and is not a valid format. Also will return an error if the email
or name fields are not present.

HttpMethod: PUT

Path: `/api/v1/users`

Input:
- request body: user fields to be updated

#### Success

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 8,
    "name": "Arthoor",
    "email": "avanhoff@sun.com"
}'
```` 
Response:

`status: 200 OK`
````
{
    "id": 8,
    "name": "Arthoor",
    "email": "avanhoff@sun.com"
}
````
#### Error Cases

Not valid or null email.

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data '{
    "id": 8,
    "name": "Arthur"
}'
```` 
Response:

`status: 400 Bad Request`
````
{
    "error": "Bad Request",
    "message": "email format is not valid",
    "timestamp": "2023-05-31T23:11:41.904",
    "status": 400
}
````

Not valid or null user name.

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/users' \
--header 'Content-Type: application/json' \
--data '{
    "id": 8,
    "email": "avanhoffsun.com"
}'
```` 
Response:

`status: 400 Bad Request`
````
{
    "error": "Bad Request",
    "message": "field name is not valid",
    "timestamp": "2023-05-31T23:14:27.610",
    "status": 400
}
````

### Delete User

HttpMethod: DELETE

Path: `/api/v1/users/{userId}`

Input:
- userId: user unique identifier

#### Success
````
curl --location --request DELETE 'localhost:9091/api/v1/users/1'
````
Response:

`Status: 204 No Content`

#### Error Cases

Request:
````
curl --location --request DELETE 'localhost:9091/api/v1/users/9898'
````
Response:
`status: 404 Not Found`
````
{
    "error": "Not Found",
    "message": "user with id=9898 not found",
    "timestamp": "2023-05-31T23:18:24.375",
    "status": 404
}
````

## Project operations
### Create Project
Will create a new project entity in the database.
If the name is missing it will return an error.

HttpMethod: POST

Path: `/api/v1/projects`

Input:
- request body: project entity fields name and description.

#### Success

Request:
````
curl --location 'localhost:9091/api/v1/projects' \
--header 'Content-Type: application/json' \
--data '{
    "name": "JEP-452",
    "description": "Key encapsulation mechanism API"
}'
```` 
Response:

`status: 200 OK`
````
{
    "id": 1,
    "name": "JEP-452",
    "description": "Key encapsulation mechanism API"
}
```` 

#### Error cases

Missing name field.

Request:
````
curl --location 'localhost:9091/api/v1/projects' \
--header 'Content-Type: application/json' \
--data '{
    "description": "Key encapsulation mechanism API"
}'
```` 
Response:

`status: 400 Bad Request`
````
{
    "error": "Bad Request",
    "message": "not valid project name",
    "timestamp": "2023-05-31T23:23:18.691",
    "status": 400
}
````
### Find Project by id
Search a project based on the id. Will return a project with all its fields.

HttpMethod: GET

Path: `/api/v1/projects/{projectId}`

Input:
- projectId: project unique identifier.

#### Success
Request:
````
curl --location 'localhost:9091/api/v1/projects/1'
````
Response:
`status: 200 OK`
````
{
    "id": 1,
    "name": "JEP-143",
    "description": "Improved contended locking",
    "users": [
        {
            "id": 1,
            "name": "Rob",
            "email": "robpike@google.com"
        },
        {
            "id": 2,
            "name": "James",
            "email": "jgosling@sun.com"
        }
    ]
}
````
#### Error Cases
Request:
````
curl --location 'localhost:9091/api/v1/projects/121'
````
Response:
`status: 200 OK`
````
{
    "timestamp": "2023-05-31T23:30:46.660",
    "message": "project not found",
    "error": "Not Found",
    "status": 404
}
````
### Find Project by name
Search a project based on name.

HttpMethod: GET

Path: `/api/v1/projects?name={projectName}`

Input:
- projectName: project name.

#### Success
Request:
````
curl --location 'localhost:9091/api/v1/projects?name=JEP-323'
````
Response:
`status: 200 OK`
````
{
    "id": 9,
    "name": "JEP-323",
    "description": "Local-variable syntax for lambda parameters"
}
````
#### Error Cases
Request:
````
curl --location 'localhost:9091/api/v1/projects?name=foo'
````
Response:
`status: 200 OK`
````
{
    "timestamp": "2023-05-31T22:30:46.660",
    "message": "project not found",
    "error": "Not Found",
    "status": 404
}
````
### Update Project
This API will update project fields based on the given input.
If the project is not existing will return an error.

Update project name.

HttpMethod: PUT

Path: `/api/v1/projects/{projectId}`

Input:
- projectId: project unique identifier
- request body: project fields to be updated.

#### Success
````
curl --location --request PUT 'localhost:9091/api/v1/projects/9' \
--header 'Content-Type: application/json' \
--data '{
    "name": "JEP-3233"
}'
```` 
Response:

`status: 200 OK`
````
{
    "id": 9,
    "name": "JEP-3233",
    "description": "Local-variable syntax for lambda parameters",
    "users": [
        {
            "id": 4,
            "name": "Max",
            "email": "mbonfit@google.com"
        },
        {
            "id": 11,
            "name": "Ken",
            "email": "kthompson@sun.com"
        },
        {
            "id": 13,
            "name": "Bill",
            "email": "bkennedy@google.com"
        }
    ]
}
````
Update project description.
````
curl --location --request PUT 'localhost:9091/api/v1/projects/9' \
--header 'Content-Type: application/json' \
--data '{
    "description": "Local-variable syntax for lambda parameters and streams"
}'
```` 
Response:

`status: 200 OK`
````
{
    "id": 9,
    "name": "JEP-3233",
    "description": "Local-variable syntax for lambda parameters and streams",
    "users": [
        {
            "id": 4,
            "name": "Max",
            "email": "mbonfit@google.com"
        },
        {
            "id": 11,
            "name": "Ken",
            "email": "kthompson@sun.com"
        },
        {
            "id": 13,
            "name": "Bill",
            "email": "bkennedy@google.com"
        }
    ]
}
````
#### Error cases

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/projects/93' \
--header 'Content-Type: application/json' \
--data '{
    "description": "Local-variable syntax for lambda parameters and streams"
}'
```` 
Response:

`status: 404 Not Found`
````
{
    "status": 404,
    "timestamp": "2023-05-31T22:11:31.139",
    "message": "project not found",
    "error": "Not Found"
}
````
### Assign Users to Project
Assigns users to a project based on existing user id/s. Project id and an array of user id/s should be provided.
If any of the users are not found in the database an error will be returned with an array of such id/s.

To assign successfully the project and all users should exist in the database.

HttpMethod: PUT

Path: `/api/v1/projects/{projectId}/users/assign`

Input:
- projectId: project unique identifier
- request body: array of numbers in the format [1, 2,..., n] representing id/s to be assigned.

#### Success

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/projects/9/users/assign' \
--header 'Content-Type: application/json' \
--data '[
    11,
    13,
    4
]'
```` 
Response:

`status: 200 OK`
````
{
    "id": 9,
    "name": "JEP-323",
    "description": "Local-variable syntax for lambda parameters",
    "users": [
        {
            "id": 4,
            "name": "Max",
            "email": "mbonfit@google.com"
        },
        {
            "id": 11,
            "name": "Ken",
            "email": "kthompson@sun.com"
        },
        {
            "id": 13,
            "name": "Bill",
            "email": "bkennedy@google.com"
        }
    ]
}
````

#### Error cases

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/projects/9/users/unassign' \
--header 'Content-Type: application/json' \
--data '[
    345,
    99
]'
```` 
Response:

`status: 404 Not Found`
````
{
    "timestamp": "2023-05-31T21:26:44.460",
    "message": "users id/s [345,99] were not found and not added to the project",
    "error": "Not Found",
    "status": 404
}
````
### Unassign User/s from Project
In the opposite way of the assign operation this API will unassign a given set
of user id/s from a certain project.
If any of the given id/s are not existing or are not assigned to the project
it will return an error with the id/s that are not found in the records or
assigned to the project.

To unassign users successfully they should all exist in the records and be
assigned to a project already. Also the project should be valid.

HttpMethod: PUT

Path: `/api/v1/projects/{projectId}/users/unassign`

Input:
- projectId: project unique identifier
- request body: array of numbers in the format [1, 2,..., n] representing id/s to be unassigned

#### Success

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/projects/9/users/unassign' \
--header 'Content-Type: application/json' \
--data '[
    11
]'
```` 
Response:

`status: 200 OK`
````
{
    "id": 9,
    "name": "JEP-323",
    "description": "Local-variable syntax for lambda parameters",
    "users": [
        {
            "id": 4,
            "name": "Max",
            "email": "mbonfit@google.com"
        },
        {
            "id": 13,
            "name": "Bill",
            "email": "bkennedy@google.com"
        }
    ]
}
````

#### Error cases

Request:
````
curl --location --request PUT 'localhost:9091/api/v1/projects/9/users/unassign' \
--header 'Content-Type: application/json' \
--data '[
    1111,
    455,
    56
]'
```` 
Response:

`status: 404 Not Found`
````
{
    "timestamp": "2023-05-31T21:26:44.460",
    "message": "users id/s [1111,455,56] were not found and not added to the project",
    "error": "Not Found",
    "status": 404
}
````

### Delete Project

HttpMethod: DELETE

Path: `/api/v1/projects/{projectId}`

Input:
- projectId: project unique identifier

#### Success

Request:
````
curl --location --request DELETE 'localhost:9091/api/v1/projects/7'
```` 
Response:

`status: 204 No Content`

#### Error cases

Request:
````
curl --location --request DELETE 'localhost:9091/api/v1/projects/99'
```` 
Response:

`status: 404 Not Found`
````
{
    "status": 404,
    "error": "Not Found",
    "message": "project not found",
    "timestamp": "2023-05-31T22:29:11.483"
}
````

## Tests

Running unit tests can be done with the following command:
````
mvn clean test
````

### Additional documentation

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)