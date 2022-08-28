## Framework
Springboot

## How to use
### Execute in IDE
Import project into idea / eclipse, directly execute main method in class 'com.example.dbstask.DbsTaskApplication'

## Git Link
Git link: https://github.com/lingyan1985/dbs-task/pull/1/files

## Restful API
In src folder, under package controller, and API is GET /repositories/{owner}/{repository-name}

Response success sample:
{
    "status": "success",
    "data": {
        "createdAt": "2022-08-23T12:55:26Z",
        "fullName": "lingyan1985/test",
        "stars": 0,
        "cloneUrl": "https://github.com/lingyan1985/test.git"
    }
}

Response failed sample:
{
    "status": "error",
    "data": "Failed to get the response"
}

## Security concern
Add AES (Advanced Encryption Standard) method for Redis password encryption & decryption
