package com.laptop.shopping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.laptop.shopping.restful.TestUtil;
import org.junit.jupiter.api.Test;

class WarrantyDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarrantyDetails.class);
        WarrantyDetails warrantyDetails1 = new WarrantyDetails();
        warrantyDetails1.setId(1L);
        WarrantyDetails warrantyDetails2 = new WarrantyDetails();
        warrantyDetails2.setId(warrantyDetails1.getId());
        assertThat(warrantyDetails1).isEqualTo(warrantyDetails2);
        warrantyDetails2.setId(2L);
        assertThat(warrantyDetails1).isNotEqualTo(warrantyDetails2);
        warrantyDetails1.setId(null);
        assertThat(warrantyDetails1).isNotEqualTo(warrantyDetails2);
    }
}
