package com.busify.project.refund.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class NetworkUtil {

    /**
     * Lấy real IP address từ HTTP request
     */
    public static String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes == null) {
                return "127.0.0.1"; // Default fallback
            }

            HttpServletRequest request = attributes.getRequest();
            return getClientIpAddress(request);

        } catch (Exception e) {
            return "127.0.0.1"; // Default fallback
        }
    }

    /**
     * Lấy real IP address từ HttpServletRequest
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr != null && !remoteAddr.isEmpty() && !"unknown".equalsIgnoreCase(remoteAddr)) {
            return remoteAddr;
        }

        return "127.0.0.1"; // Default fallback
    }
}
