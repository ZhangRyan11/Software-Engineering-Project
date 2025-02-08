# Integer Computation System

## Computation Description

This system calculates the factorization of input integers. For a given integer input, it will output all factors of that number.

### Example
- **Input:** `8`
- **Output:** `1, 2, 4, 8`

## Features
- Accepts a positive integer as input.
- Computes and returns all factors of the number.
- Handles edge cases such as `1` (which only has itself as a factor).
- Optimized to run efficiently for large integers.

## How It Works
1. The system iterates from `1` to `n` (input number).
2. For each number `i`, it checks if `n % i == 0`.
3. If true, `i` is added to the list of factors.
4. The list of factors is returned as output.

## Usage
### Running the System
```bash
java Main
