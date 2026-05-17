package com.taekwondoraji_api.domain.attendance.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

@Service
public class AttendanceQrService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int DEFAULT_QR_SIZE = 280;

    private final String secret;

    public AttendanceQrService(
            @Value("${attendance.qr.secret}") String secret
    ) {
        this.secret = secret;
    }

    public String createTodayQrSvg(Integer gymId) {
        return createQrSvg(createTodayPayload(gymId), DEFAULT_QR_SIZE);
    }

    public String createTodayPayload(Integer gymId) {
        LocalDate today = LocalDate.now();
        String token = createToken(gymId, today);

        return "{\"type\":\"attendance\",\"gymId\":%d,\"date\":\"%s\",\"token\":\"%s\"}"
                .formatted(gymId, today, token);
    }

    private String createToken(Integer gymId, LocalDate date) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            byte[] digest = mac.doFinal((gymId + ":" + date).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String createQrSvg(String payload, int size) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    payload,
                    BarcodeFormat.QR_CODE,
                    size,
                    size,
                    Map.of(EncodeHintType.MARGIN, 1)
            );

            StringBuilder svg = new StringBuilder();
            svg.append("""
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 %d %d" width="%d" height="%d" shape-rendering="crispEdges">
                    <rect width="100%%" height="100%%" fill="#ffffff"/>
                    """.formatted(size, size, size, size));

            for (int y = 0; y < matrix.getHeight(); y += 1) {
                for (int x = 0; x < matrix.getWidth(); x += 1) {
                    if (matrix.get(x, y)) {
                        svg.append("<rect x=\"")
                                .append(x)
                                .append("\" y=\"")
                                .append(y)
                                .append("\" width=\"1\" height=\"1\" fill=\"#111827\"/>");
                    }
                }
            }

            svg.append("</svg>");
            return svg.toString();
        } catch (WriterException exception) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
