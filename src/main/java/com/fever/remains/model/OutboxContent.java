package com.fever.remains.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class OutboxContent {
	private GetContentResponse getContentResponse;
	private Long feedId;
}