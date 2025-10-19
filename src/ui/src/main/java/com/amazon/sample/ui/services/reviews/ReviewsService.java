/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: MIT-0
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.amazon.sample.ui.services.reviews;

import com.amazon.sample.ui.config.EndpointProperties;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ReviewsService {

  private final WebClient webClient;

  public ReviewsService(EndpointProperties endpoints) {
    this.webClient = WebClient.builder().baseUrl(endpoints.getReviews()).build();
  }

  @SuppressWarnings("unchecked")
  public Mono<List<Map<String, Object>>> getReviews(String productId) {
    return webClient
      .get()
      .uri("/reviews/{productId}", productId)
      .retrieve()
      .bodyToFlux(Map.class)
      .collectList()
      .map(list -> (List<Map<String, Object>>) (List<?>) list)
      .onErrorResume(e -> {
        log.error("Error fetching reviews for product {}", productId, e);
        return Mono.just(List.of());
      });
  }

  public Mono<Void> addReview(String productId, Map<String, Object> review) {
    return webClient
      .post()
      .uri("/reviews/{productId}", productId)
      .bodyValue(review)
      .retrieve()
      .bodyToMono(Void.class)
      .onErrorResume(e -> {
        log.error("Error adding review for product {}", productId, e);
        return Mono.empty();
      });
  }
}
