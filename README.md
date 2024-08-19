# dxc-market

##### Table of Contents

- [Description](#description)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [RESTful Services](#restful-services)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Usage](#usage)

## Description

The DXC Market Management System is a project that provides a platform for managing customers, products, orders,
refunds, and complaints. It offers a set of RESTful services to perform various operations on these entities.

## Features

- Add and retrieve customer information.
- Add and retrieve product details.
- Place orders and retrieve order details.
- Process refunds and retrieve refund information.
- Register complaints against orders or refunds.
- Implement update and delete operations for customers and products.
- Validate operations to ensure data integrity.

## Technologies Used

- Programming Language: [Java]
- Web Framework: [Spring boot]
- Database: [MySQL]

## RESTful Services

The project includes the following RESTful services:

- **order Service**
  - GET /orders?id=1: Retrieve order by ID
  - GET /orders?customerId: Retrieve all orders by customer ID
  - POST /orders: Create a new order for specific customer 

## Getting Started

### Prerequisites

### Installation

### Usage