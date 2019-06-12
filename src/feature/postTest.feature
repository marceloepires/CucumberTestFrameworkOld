@InScope
@wip
Feature: RestTest1

  Scenario: Communicate to Google API using POST JSON

    Given I create the google place in Json
    When I delete the google place in Json

  Scenario: Communicate to Google API using POST XML

    Given I create the google place in XML
    When I delete the google place in XML
