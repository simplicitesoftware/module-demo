This file provides guidance for Copilot when working on this Simplicité low code platform module.

## Project Overview

- Java classes sources are in the `src` directory
- Javascript, CSS and LESS sources are in the `resources` directory
- Everything in the `others` directory must be ignored

## General

- Prefer explicit names over implicit
- The business objects names and their fields names are available in the `Demo.md` file

## Programming Language: Java

- Some relevant code examples are available at this URL: `https://docs.simplicite.io//category/core`
- The applicable Javadoc is available at this URL: `https://platform.simplicite.io//7.0/javadoc`
- Follow standard Java naming conventions: `camelCase` for methods and variables, `PascalCase` for classes, `UPPER_SNAKE_CASE` for constants
- Maximum line length: **120 characters**
- Use 4-space indentation (no tabs)
- Always add `@Override` annotation when overriding methods
- Avoid raw types — always parameterize generics (e.g., `List<String>` not `List`)
- Avoid using var `var` except when the type is obvious from the right-hand side
- Prefer streams and lambdas over imperative loops where readability is maintained
- Use only the methods of the `com.simplicite.util.AppLog` class for Logging
- Never use other logging classes
- Prefer exceptions extending `com.simplicite.exceptions.PlatformException`
- Never swallow exceptions silently (`catch (Exception e) {}` is forbidden)
- Never add new Maven dependencies

## Programming Language: JavaScript

- Use **ES2020+** syntax
- Use `camelCase` for functions and variables, `PascalCase` for classes
- Use `const` by default, `let` only when reassignment is necessary, never use `var`
- Use 4-space indentation (no tabs)
- Use single quotes for strings
- Maximum line length: **120 characters**
- Prefer arrow functions for callbacks and short functions
- Use destructuring for objects and arrays whenever practical
- Use optional chaining (`?.`) and nullish coalescing (`??`) instead of verbose null checks
- No `console.log` in production code — use `$console`
- Wrap async calls in `try/catch` or handle `.catch()` on promises
- Never ignore a rejected promise
- Never add new packages nor change versions in `package.json`

## Programming Language: CSS or LESS

- Use `snakeCase` for classes names and IDs
- Use 4-space indentation (no tabs)
- Maximum line length: **120 characters**
- No more that 5 rule per line
