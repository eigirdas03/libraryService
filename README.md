# Library web service
Simple library web service that implements CRUD operations 

## How to run
1. `git clone https://github.com/eigirdas03/libraryService`
2. `cd libraryService`
3. `docker-compose up --build` or `docker-compose up --build -d` (detached mode)
4. open browser and go to http://localhost:80/swagger
   

## Resources

### Library
* id (auto generated)
* name
* address
* opened (year)
* books[] (book resource array, in POST/PUT body book IDs are written here, book object with that id is automatically added to the array)

### Book

* id (auto generated)
* author (name + surname)
* title
* published (year)

## Available commands

### GET
* http://localhost:80/libraries - get all libraries
* http://localhost:80/libraries/1 - get specific library
* http://localhost:80/books - get all books
* http://localhost:80/books/1 - get specific book

### POST
* http://localhost:80/libraries - create a library
* http://localhost:80/books - create a book

### PUT
* http://localhost:80/libraries/1 - update specific library
* http://localhost:80/books/1 - update specific book
* http://localhost:80/libraries/1/books - update specific library "books" array

### DELETE
* http://localhost:80/libraries/1 - delete specific library
* http://localhost:80/books/1 - delete specific book
* http://localhost:80/libraries/1/books/2 - remove book(id 2) from library(id 1)


## POST/PUT body structures


### Library
* library id will be ignored
* "books" array cannot be empty in PUT commands

#### POST/PUT

```json
{
    "name": "library1",
    "address": "address 1-2",
    "opened": "1990",
    "books": [1, 2]
}
```

#### PUT (http://localhost:80/libraries/1/books)
```json
{
    "books": [1, 3]
}
```

### Book
* book id will be ignored

#### POST/PUT
```json
{
    "author": "name1 surname1",
    "title": "title1",
    "published": "1991"
}
```


