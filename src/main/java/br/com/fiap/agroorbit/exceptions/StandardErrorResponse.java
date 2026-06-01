package br.com.fiap.agroorbit.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record StandardErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path, List<String> details) {}
