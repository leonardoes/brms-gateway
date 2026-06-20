# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot gateway application for the Globalvida insurance business rules engine (BRMS). It processes insurance quotation requests by validating business rules using Drools and calling external calculation engines.

## Build and Run Commands

```bash
# Build the project
mvn clean install

# Run locally
mvn spring-boot:run

# Build Docker image
docker build -t brms-gateway-globalvida-cotacao .

# Package as JAR
mvn package
```

## Architecture

### Core Components

1. **Gateway Layer** (`gateway.web.servicos`)
   - `GlobalvidaService`: Main REST controller handling `/rest/calculoGlobalvida` endpoint
   - Orchestrates rule validation and premium calculations for two groups: Funcionarios (employees) and Socios (partners)
   - Integrates with external BRMS engines for calculations

2. **Drools Rules Engine**
   - Local validation rules in `/src/main/resources/rules/`
   - Three rule sets: funcionarios-interdependencies, socios-interdependencies, vidas-limit
   - Validates coverage dependencies and business constraints before external API calls
   - Rules return `RetornoRegra` objects with error codes and messages

3. **Request/Response Flow**
   - Receives `RequestGlobalvida` with coverage and assistance selections
   - Fires local Drools rules for validation
   - Calls external engines for premium calculation
   - Returns `ResponseGlobalvida` with premiums and any rule violations

### Key Business Logic

- **Coverage Interdependencies**: Certain coverages require others (e.g., IEA requires M, MAF requires MACONJ)
- **Mutual Exclusions**: Some coverages cannot be combined (e.g., M and MA are mutually exclusive)
- **Group Restrictions**: Corporate assistance only for Socios, not Funcionarios
- **Vidas Limits**: Minimum 2 lives, maximum 499 lives total

### External Integrations

- Calculation endpoints configured in `application.properties`:
  - `app.brms.calculocotacao`: Coverage premium calculations
  - `app.brms.calculoassistencia`: Assistance premium calculations
  - `app.brms.regraaceitacao`: Acceptance rules (currently unused in code)

## Important Files

- `GlobalvidaService.java`: Main service orchestrating the calculation flow
- `DroolsService.java`: Manages Drools rule sessions
- `kmodule.xml`: Drools configuration defining rule packages and sessions
- `funcionarios-interdependencies.drl`: Coverage dependency rules for employees
- `socios-interdependencies.drl`: Coverage dependency rules for partners
- `vidas-limit.drl`: Rules for minimum/maximum lives validation

## Code Mapping Layer

- `CoberturaConverter`: Bidirectional mapping utility between numeric coverage codes (e.g., "13281") and business acronyms (e.g., "M")
  - Used to convert incoming requests from acronyms to codes before calling external engines
  - Converts responses back from codes to acronyms for client consumption
  - All mappings are statically initialized in HashMap structures

## Development Notes

- Java 17 required
- Spring Boot 3.5.6 with Undertow (not Tomcat)
- Drools 7.74.1.Final for rules engine
- Lombok for reducing boilerplate code
- SpringDoc OpenAPI for API documentation (available at `/swagger-ui.html`)
- PostgreSQL database configuration exists but is currently disabled in `application.properties`
- AWS Secrets Manager integration available but commented out
- No test files currently in the project
- WebFlux included for reactive HTTP client calls to external engines