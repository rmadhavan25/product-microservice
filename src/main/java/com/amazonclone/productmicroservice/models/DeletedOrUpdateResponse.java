package com.amazonclone.productmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeletedOrUpdateResponse {

		private boolean isSuccessful;
		private String description;
}
