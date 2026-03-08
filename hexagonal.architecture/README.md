# Hexagonal Architecture Modular Monolith

Small but opinionated SaaS-like application showing how to structure a modular monolith with Spring Boot 4, Java 25 and Gradle Kotlin DSL. The focus is educational clarity: each module owns its domain, application layer and infrastructure adapters so it is obvious where logic belongs.

## Modules

```
com.example.app
├── shared.kernel
│   ├── domain – cross-cutting domain primitives such as `DomainEvent`
│   └── application – shared error types (`BusinessRuleViolation`)
├── identity – owns tenant/contact details
├── subscriptions – owns the subscription aggregate and renewal policy
├── billing – orchestrates billing use cases + read/query side
└── notification – owns notification semantics and outbound email capability
```

Each module follows the package convention:

```
module
├── domain (model/event/service)
├── application (port.in, port.out, service, query)
└── infrastructure (adapter/in, adapter/out, config)
```

## Layer responsibilities

- **Domain**: business concepts, invariants, domain events, pure domain services (e.g. `Subscription`, `StandardMonthlyRenewalPolicy`). No Spring annotations, no repositories.
- **Application**: use cases, inbound ports, outbound ports, orchestration, transaction boundaries (`ProcessDueSubscriptionsService`). Contracts for scalable reads live here (e.g. `DueSubscriptionStreamPort`, `OverdueBillingItemsQuery`).
- **Infrastructure**: adapters that translate HTTP/jobs/messaging/persistence/email concerns into the ports (`BillingController`, `InMemoryDueSubscriptionAdapter`, `NotificationModuleAdapter`). DTOs and JPA/in-memory entities stay here because they belong to boundary technology.

Dependency direction is strictly **infrastructure → application → domain**. Modules talk to each other only through application ports.

## Implemented features

### Billing processing use case
- Inbound ports: `ProcessDueSubscriptionsUseCase` (called by REST controller + scheduled job).
- Outbound ports: `DueSubscriptionStreamPort` (batch/stream contract), `ChargeSubscriptionPort`, `PersistSubscriptionStatePort`, `BillingNotificationPort`.
- Service orchestration: `ProcessDueSubscriptionsService` handles transactional workflow, delegates business rules to the `Subscription` aggregate and `RenewalPolicy`, and emits notifications.

### Read/query use case
- `ListOverdueBillingItemsQuery` lives in `billing.application.query` and returns `OverdueBillingItemView`, a projection intentionally different from the domain aggregate.
- Infrastructure adapter `InMemoryOverdueBillingItemsAdapter` shows how read models can be optimized separately from write models.

### Notification capability
- `NotificationApplicationService` implements `SendNotificationUseCase` and depends on outbound port `EmailSenderPort`.
- `LoggingEmailSender` is a simple outbound adapter; replacing it with SMTP only touches infrastructure.
- Billing module depends on notification through the outbound adapter `NotificationModuleAdapter`, keeping dependency direction correct.

### Identity basics
- `TenantId` and `CustomerContact` live in the identity domain.
- Application service `CustomerDirectoryService` implements `FindCustomerContactQuery` using in-memory persistence.

-### Subscriptions domain & use cases
- Aggregate `Subscription` protects invariants for renewal and past-due transitions, emitting domain events `SubscriptionRenewed` and `SubscriptionMarkedPastDue`.
- Domain service `StandardMonthlyRenewalPolicy` demonstrates when to use a domain service (rule that spans aggregates).
- Application layer now includes `RegisterSubscriptionUseCase` + `SubscriptionApplicationService`, showing how modules expose their own commands without leaking HTTP/DB details.

### Infrastructure examples
- REST controller (`BillingController`) and scheduled job (`ProcessDueSubscriptionsJob`) are inbound adapters.
- Outbound adapters exist for persistence, simulated payments, notification module delegation and read projections. Comments explain why adapters live in infrastructure even when they look like repositories.

## Running & testing

Requirements: JDK 25 and Maven (wrapper included).

```bash
# run the application
./mvnw spring-boot:run

# execute tests
./mvnw test
```

The sample ships with in-memory data bootstrap so endpoints respond immediately:
- `POST /billing/process` – trigger billing for today
- `GET /billing/overdue?limit=5` – list overdue billing projections

## Tests included
1. **Domain test** (`SubscriptionTest`) verifies renewal rules without Spring.
2. **Application test** (`ProcessDueSubscriptionsServiceTest`) uses fake adapters showing how use cases stay testable without wiring.
3. **Web adapter test** (`BillingControllerTest`) demonstrates testing only the adapter with mocked use cases.

## Why the query package exists
Some read use cases (like listing overdue billing items) need projection models to avoid leaking aggregates and to support scalable storage strategies. Keeping them under `application/query` makes this intent explicit and reminds developers that read contracts may evolve independently from write models.

## Why a domain service exists
`StandardMonthlyRenewalPolicy` encapsulates the otherwise duplicated rule of how renewal dates advance. It belongs to the domain because it is pure business logic and could later be replaced with plan-specific strategies without touching application services.

## Common misconceptions corrected by this example
- **Domain ≠ use cases**: Domain contains `Subscription`, value objects, events and policies. Use cases live in `billing.application`.
- **Ports live in application**: interfaces such as `DueSubscriptionStreamPort`, `EmailSenderPort`, and `FindCustomerContactQuery` are defined next to their use cases, not in infrastructure.
- **Adapters belong to infrastructure**: controllers, scheduled jobs, repositories, email senders and module bridges live under `infrastructure.adapter.*` packages.
- **Inbound vs outbound adapters**: REST controller + job call inbound port `ProcessDueSubscriptionsUseCase`; persistence/email adapters implement outbound ports.
- **Dependency direction**: infrastructure depends on application, which depends on domain. Domain has zero Spring annotations.
- **Domain holds business rules, not orchestration**: `Subscription` enforces renewal rules. `ProcessDueSubscriptionsService` orchestrates transactions and ports.
- **Use cases sit in application layer**: The billing workflow lives entirely in `ProcessDueSubscriptionsService`, not in controllers or repositories.
- **Scalable contracts belong to application**: Batch streaming of due subscriptions is modeled as an application port so infrastructure can implement it via SQL cursors.
- **Read models differ from domain models**: `OverdueBillingItemView` is a projection purposely separate from `Subscription`.
- **Ports are not just entry points**: outbound ports (payment gateway, persistence, notifications) are also ports.
- **Domain service usage**: `StandardMonthlyRenewalPolicy` shows when a domain service is justified (rule across aggregates) instead of generic utility classes.
- **CRUD-shaped ports are avoided**: outbound ports express use-case needs (`persist` with events, `streamDueSubscriptions`) rather than `save/findAll` repositories.
