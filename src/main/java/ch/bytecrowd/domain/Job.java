package ch.bytecrowd.domain;

import ch.bytecrowd.config.CustomOffsetDateTimeDeserializer;
import ch.bytecrowd.config.CustomOffsetDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String label;
    @Column
    @JsonSerialize(using = CustomOffsetDateTimeSerializer.class)
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime schedule;
    @Column
    private String payload;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job id(Long id) {
        this.setId(id);
        return this;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Job label(String label) {
        this.setLabel(label);
        return this;
    }

    public OffsetDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(OffsetDateTime schedule) {
        this.schedule = schedule;
    }

    public Job schedule(OffsetDateTime schedule) {
        this.setSchedule(schedule);
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Job payload(String payload) {
        this.setPayload(payload);
        return this;
    }
}
