# Integer Computation System

## Multi-threading Implementation

The `MultiThreadedCoordinator` class provides a multi-threaded implementation of the `NetworkAPI` interface. 
It uses a thread pool to process computation tasks in parallel.

### Thread Pool Size

The multi-threaded implementation uses a fixed thread pool with a maximum of 4 threads. This number was chosen for the following reasons:

1. **CPU Optimization**: Most modern computers have 4-8 CPU cores. Using 4 threads allows for good parallelism without oversubscribing the CPU.
2. **Diminishing Returns**: Testing showed that increasing the thread count beyond 4 provided diminishing returns for our typical workloads.
3. **Memory Efficiency**: Each thread consumes memory resources. Limiting to 4 threads helps maintain reasonable memory usage.

If your system has significantly different characteristics, you may want to adjust the `MAX_THREADS` constant in the `MultiThreadedCoordinator` class.

## Running Tests

The `TestMultiUser` class now uses the multi-threaded implementation for testing concurrent access to the computation service. The test verifies that the multi-threaded implementation produces the same results as sequential processing.

Regular smoke tests still use the single-threaded implementation to ensure deterministic behavior during basic testing.


## Computation Description

This system calculates the factorization of input integers. For a given integer input, it will output all factors of that number.

### Example
- **Input:** `8`
- **Output:** `1 2 4 8`

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

![Untitled Page-1](https://github.com/user-attachments/assets/c835f470-3b26-4f55-9f3f-210651e784a4)
