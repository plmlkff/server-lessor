package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.serverlessorback.domain.entity.enums.InvitationStatus;

import java.io.Serializable;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Invitation.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Invitation {
    public static final String TABLE_NAME = "invitation";

    @EmbeddedId
    private Key id;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private InvitationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id")
    @MapsId("refereeId")
    private User referee;

    @ManyToOne
    @JoinColumn(name = "referral_id")
    @MapsId("referralId")
    private User referral;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key implements Serializable {
        private UUID refereeId;

        private UUID referralId;
    }
}
