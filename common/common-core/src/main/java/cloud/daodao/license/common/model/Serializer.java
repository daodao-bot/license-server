package cloud.daodao.license.common.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public interface Serializer extends Serializable {

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule()
                    .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                    .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME))
                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            );

    default String toJson() {
        String json;
        try {
            json = objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    static String toJson(Object o) {
        String json;
        try {
            json = objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * 此方法不支持泛型，泛型请使用 {@link #ofJson(String, TypeReference)}
     *
     * @param <T>  T
     * @param json json
     * @return T
     */
    @SuppressWarnings("unchecked")
    default <T> T ofJson(String json) {
        T t;
        Class<? extends Serializer> clazz = this.getClass();
        try {
            t = (T) objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    static <T> T ofJson(String json, TypeReference<T> typeReference) {
        T t;
        try {
            t = objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

}
