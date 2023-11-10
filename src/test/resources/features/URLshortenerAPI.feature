Feature: API handles URL shortening

  Scenario: API receives an URL and returns a shortened version of it
    Given there is no shortened URLs saved for "https://example-url.com/foo"
    When requested to short the URL "https://example-url.com/foo"
    Then there is a shortened URL saved for URL "https://example-url.com/foo"
    And the response body contains short URL hash "21mhQHIpDicPuKWCRfhwRA=="

  Scenario: API is requested with existing short URL and redirects it to original URL
    Given there is a shortened URLs saved for "https://example-url.com/foo"
    When a request by the shorter URL is done
    And the response body is a redirection to "https://example-url.com/foo"

    # TODO:
    # make it with multiple example scenario
    # scenario with "whatever.com/example" -> "/LXy41NJt2PYI4Oxtxo9/A==" do slashes work?
    # error cases (unavailable repo, not existing shortened URL, md5 length limits, bad request...)
    # already saved url case