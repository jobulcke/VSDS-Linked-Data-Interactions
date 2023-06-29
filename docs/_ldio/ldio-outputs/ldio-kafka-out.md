---
layout: default
parent: LDIO Outputs
title: Kafka Out
---

# LDIO Kafka Out
***be.vlaanderen.informatievlaanderen.ldes.ldio.LdioKafkaOut***

The LDIO Kafka Out sends messages to a [kafka topic](https://kafka.apache.org).
Two security protocols are supported:
- [NO SECURITY](#no-security)
- [SASL SSL PLAIN](#sasl-ssl-plain)

## Config

| Property             | Description                                                         | Required | Default | Example                          | Supported values                                                                     |
|----------------------|---------------------------------------------------------------------|----------|---------|----------------------------------|--------------------------------------------------------------------------------------|
| content-type         | Any content type supported by Apache Jena                           | Yes      | N/A     | application/n-quads              | String                                                                               |
| bootstrap-servers    | Comma separated list of uris of the bootstrap servers               | Yes      | N/A     | localhost:9012                   | url                                                                                  |
| topic                | Name of the topic                                                   | Yes      | N/A     | quickstart-events                | String                                                                               |
| key-property-path    | Optional property path to extract the kafka key from the data model | No       | null    | <http://purl.org/dc/terms/title> | [ARQ property path](https://jena.apache.org/documentation/query/property_paths.html) |
| security-protocol    | Security protocol to be used to connect to the kafka broker         | No       | NO_AUTH | SASL_SSL_PLAIN                   | SASL_SSL_PLAIN or NO_AUTH                                                            |
| sasl-jaas-user       | Username used in the security protocol                              | No       | null    | client                           | String                                                                               |
| sasl-jaas-password   | Password used in the security protocol                              | No       | null    | secret                           | String                                                                               |

## Example

### NO SECURITY

```yaml
outputs:
  - name: be.vlaanderen.informatievlaanderen.ldes.ldio.LdioKafkaOut
    config:
      content-type: application/n-quads
      bootstrap-servers: localhost:9092
      topic: quickstart-events
      key-property-path: <https://purl.org/geojson/vocab#properties>/<http://purl.org/dc/terms/title>
```

### SASL SSL PLAIN

```yaml
outputs:
  - name: be.vlaanderen.informatievlaanderen.ldes.ldio.LdioKafkaOut
    config:
      content-type: application/n-quads
      bootstrap-servers: localhost:9092
      topic: quickstart-events
      key-property-path: <https://purl.org/geojson/vocab#properties>/<http://purl.org/dc/terms/title>
      security-protocol: SASL_SSL_PLAIN
      sasl-jaas-user: client
      sasl-jaas-password: client-secret
```