# Integer Computation System

## Computation Description

This system calculates the prime factorization of input integers. For a given integer input, it will output all prime factors of that number.

### Example
- Input: `12`
- Output: `12:2,2,3`
- Explanation: 12 = 2 × 2 × 3

The computation is CPU-intensive as it involves:
1. Testing divisibility up to the square root of the input number
2. Recursively finding prime factors
3. For large numbers, this becomes computationally expensive

## System Design

![System Architecture](docs/images/system-architecture.png)

The system consists of three main components:
1. User Interface Layer - Handles user input/output specifications
2. Compute Engine - Performs the prime factorization
3. Data Storage System - Manages data input/output operations

### Component Interactions
- User ↔ Compute Engine: Network boundary for job specifications
- Data Storage ↔ Compute Engine: Process boundary for data access
- Job Manager ↔ Computation Core: Conceptual boundary within compute engine
