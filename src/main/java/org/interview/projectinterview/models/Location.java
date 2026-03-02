package org.interview.projectinterview.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Column(name = "province_id")
    private Long ProvinceId;
    @Column(name = "province_name")
    private String ProvinceName;
    @Column(name = "ward_id")
    private Long WardId;
    @Column(name = "ward_name")
    private String WardName;
    @Column(name = "address")
    private String Address;
}
