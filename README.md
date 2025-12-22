# Player Controller API – Test Assignment

This project contains automated API tests for Player Controller service.
Tests are written as for a production-grade system, where any change is risky and costly.

The goal of the assignment was to:
- Design a small but robust test framework;
- cover critical functionality with automated tests;
- identify and document potential defects in the API.

# Framework Features
- Thread-safe HTTP client (supports parallel execution);
- Centralized configuration (base URL, threads, etc.);
- HTTP client layer;
- DTOs;
- Tests;
- Parallel test execution (3 threads);
- Allure reports;
- Production-oriented assertions and cleanup logic.

# Notes

* Tests are isolated and cleaned up after execution;
* Cleanup logic handles unexpected successful operations (bug-aware);
* All tests are designed with real production.

# How to Run Tests

`mvn clean test`

To generate Allure report:

`mvn allure:serve`

# Bugs Found During Testing

----

**[ISSUE] Player creation is implemented using GET method (critical design issue)**

`GET /player/create/{editor}?age=...&gender=...&login=...&password=...`

Decription:
Player creation is implemented using HTTP GET method with query parameters, instead of a request body.

**Expected:**
* Resource creation must be performed using POST;
* Request data must be passed in JSON request body;
* Sensitive fields (e.g. password) must never be sent via URL.

**Actual:**
* GET request creates a player;
* All player data is passed via query parameters;
* Password is transferred as part of the URL.

----
**[ISSUE] Player creation does not return created fields**

Decription:
API creates a player successfully, but response body contains null values for required fields:
* age
* gender
* role
* screenName

**Expected:**
Response should contain full created player data.

**Actual:**
Fields are returned as null.

----
**[ISSUE] Password validation: missing digits is ignored**

**Expected:**
400 Bad Request

**Actual:**
200 OK (player created)

**Impact:**
Password validation rules.

----

**[ISSUE] Password validation: missing letters is ignored**

**Expected:**
400 Bad Request

**Actual:**
200 OK

**Impact:**
Password validation rules.

----

**[ISSUE] Password validation: non-latin characters are accepted**

**Expected:**
400 Bad Request

**Actual:**
200 OK

**Impact:**
Password validation rules.

----

**[ISSUE] Deleting non-existent player returns incorrect status code**

**Expected:**
404 Not Found

**Actual:**
403 Forbidden

**Impact:**
Incorrect REST semantics.

----

**[ISSUE] Deleting player with invalid ID type**

**Expected:**
400 Bad Request

**Actual:**
204 No Content

**Impact:**
Possible to delete player by id in String format

----

**[ISSUE] Player list contains invalid data**

**Issue:**
Some players contain invalid values (e.g. non-positive age - 0).

**Impact:**
Data integrity.

---

**[ISSUE] Update endpoint ignores invalid data**

**Expected:**
400 Bad Request

**Actual:**
200 OK

**Impact:**
Allows corrupting existing player data.







