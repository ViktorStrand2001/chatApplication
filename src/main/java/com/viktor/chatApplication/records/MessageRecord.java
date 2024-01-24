package com.viktor.chatApplication.records;

import com.viktor.chatApplication.enums.Action;
import com.viktor.chatApplication.models.UserModel;

import java.time.Instant;

public record MessageRecord(UserModel userModel, String comment, Action action, Instant timestamp) {
}
