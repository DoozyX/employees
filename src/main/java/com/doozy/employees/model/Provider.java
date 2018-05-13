package com.doozy.employees.model;

public enum Provider {
	LOCAL {
		@Override
		public String getLoginUrl() {
			return "/login";
		}
	},
	GITHUB {
		@Override
		public String getLoginUrl() {
			return "/login/github";
		}
	},
	GOOGLE {
		@Override
		public String getLoginUrl() {
			return "/login/google";
		}
	},
	FACEBOOK {
		@Override
		public String getLoginUrl() {
			return "/login/facebook";
		}
	};

	public String getLoginUrl() {
		return null;
	}
}

