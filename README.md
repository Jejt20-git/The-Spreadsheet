# Spreadsheet Project (Java)

## Overview
This project implements a spreadsheet application in Java. It supports numeric cells, text cells, and formula evaluation with cell references, ranges, and basic functions, like min, max, sum, average. The design follows the coursework specification provided and was done in stages of the suggested steps as described by the Professor. A clear separation of concerns between parsing, data modeling, evaluation, dependency tracking, and user interaction is implemented.

The repository also includes archived past iterations, preserved to document the project’s incremental development based on suggested steps. Changes have been made throughout to certain classes, especially those first created, such as the sv2Parser and sv2Serializer. The brackets at the end of each folder name shows the number of the suggested step by the professor that the project relates to at that stage.

---

## Key Features
- Cell types:
  - Numeric values
  - Text values
  - Formulas
- Formula support:
  - Arithmetic operations (`+`, `-`, `*`, `/`)
  - Cell references (e.g. `A1`)
  - Ranges (e.g. `A1;A10`)
  - Functions: `SUM`, `AVERAGE`, `MIN`, `MAX`
- Automatic dependency tracking and recalculation
- File import/export using the `.sv2` format
- Unit and integration tests using JUnit
- Maven project structure

---

## Project Structure

src/main/java

 ├─ spreadsheet
 
 │   ├─ Spreadsheet, Cell, Coordinate
 
 │   ├─ Content (NumericContent, TextContent, FormulaContent)
 
 │   ├─ Expression hierarchy (BinaryOp, FunctionCall, RangeRef, Literal, etc.)
 
 │   ├─ Evaluator
 
 │   ├─ DependencyTracker
 
 │   ├─ SpreadsheetFactory
 
 │   └─ IO (sv2Parser, sv2Serializer)
 
 └─ ui
     └─ SpreadsheetUI, Commands, RefParser

src/test/java

 └─ Unit and integration tests

The UI is alongisde the spreadsheet folder and not in it as described by the professor in the final class session. 

---

## Design Notes
- **Parser integration** - Implementation of the proffesors provided parser functions, it is handled by `SpreadsheetFactory`, which implements the provided `AbstractFactory` interface.  
  This allows parsed expressions to be converted directly into the project’s internal expression tree.
- **Expressions** are evaluated recursively using the `Evaluator`, keeping parsing and evaluation separated.
- **Dependency tracking** ensures that when a cell value changes, all dependent cells are recalculated automatically.
- The **UI layer** is isolated from the spreadsheet logic to keep the core model testable and extensible.

---

## Archived Versions
The `Past Iterations Per Suggested Steps/` directory contains earlier versions of the project, preserved intentionally to show the evolution of the design and feature set. These folders are **not part of the final build**, but serve as documentation of development progress.

---

## Building & Running
This is a Maven project.

To run tests:

`mvn test`

To build:

`mvn package`


