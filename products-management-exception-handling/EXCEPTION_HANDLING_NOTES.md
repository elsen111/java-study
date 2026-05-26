# Exception Handling Update

This project now has a cleaner custom exception flow for service-level validation.

## Added

- `FieldValidationException`
  - Used when one or more request fields are invalid.
  - Stores a general message and a `fieldErrors` map.

- `ResourceNotFoundException`
  - Used for missing resources such as categories.

- Updated `ErrorResponse`
  - Supports both a general `message` and field-level validation details.

- Updated `GlobalExceptionHandler`
  - Uses `@RestControllerAdvice`
  - Uses `@ExceptionHandler`
  - Handles:
    - `BadRequestException`
    - `FieldValidationException`
    - `ProductNotFoundException`
    - `ResourceNotFoundException`
    - `MethodArgumentNotValidException`
    - `HttpMessageNotReadableException`

- Updated `ValidationUtil`
  - Reusable methods for required fields, blank strings, empty lists, and negative values.

## Example Bad Request Response

```json
{
  "timestamp": "2026-05-16T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Please check the highlighted fields",
  "fieldErrors": {
    "name": "Product name is required",
    "price": "Price is required",
    "quantity": "Quantity is required",
    "categoryIds": "At least one category id is required"
  }
}
```

## Main changed files

- `src/main/java/com/example/productapi/exception/ErrorResponse.java`
- `src/main/java/com/example/productapi/exception/FieldValidationException.java`
- `src/main/java/com/example/productapi/exception/ResourceNotFoundException.java`
- `src/main/java/com/example/productapi/exception/GlobalExceptionHandler.java`
- `src/main/java/com/example/productapi/services/utils/ValidationUtil.java`
- `src/main/java/com/example/productapi/services/ProductService.java`
- `src/main/java/com/example/productapi/services/CategoryService.java`
