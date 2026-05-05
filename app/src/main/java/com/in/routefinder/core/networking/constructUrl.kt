package com.`in`.routefinder.core.networking

import com.`in`.routefinder.BuildConfig


/**
 * Constructs a full URL by prepending the base URL if necessary.
 *
 * This function takes a URL string as input and checks if it's already a full URL
 * (i.e., it contains the base URL) or if it's a relative URL. If it's a relative URL,
 * it prepends the `BuildConfig.BASE_URL` to create a full URL.
 *
 * If the provided URL starts with a forward slash ("/"), it's considered a relative path
 * and the base URL is directly prepended. Otherwise, a forward slash is inserted between the
 * base URL and the provided URL to ensure correct path separation.
 *
 * @param url The URL string to be processed. It can be a full URL or a relative path.
 * @return The constructed full URL string.
 *
 * @example
 * ```kotlin
 * // Assuming BuildConfig.BASE_URL = "https://api.example.com"
 *
 * // Full URL:
 * val fullUrl1 = constructUrl("https://api.example.com/users") // Returns "https://api.example.com/users"
 *
 * // Relative URL starting with '/':
 * val fullUrl2 = constructUrl("/products/123") // Returns "https://api.example.com/products/123"
 *
 * // Relative URL not starting with '/':
 * val fullUrl3 = constructUrl("posts") // Returns "https://api.example.com/posts"
 *
 * val fullUrl4 = constructUrl("some/other/path") // Returns "https://api.example.com/some/other/path"
 *
 * ```
 */
fun constructUrl(url: String): String {
	return when  {
		url.contains(BuildConfig.BASE_URL) -> url
		url.startsWith("/") -> BuildConfig.BASE_URL + url
		else -> BuildConfig.BASE_URL + "/" + url
	}
}