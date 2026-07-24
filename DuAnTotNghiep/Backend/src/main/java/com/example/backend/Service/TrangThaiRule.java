package com.example.backend.Service;

import com.example.backend.Entity.TrangThaiHoaDon;

import java.util.List;
import java.util.Map;

public class TrangThaiRule {

    public static final Map<TrangThaiHoaDon, List<TrangThaiHoaDon>> ALLOWED = Map.of(

            TrangThaiHoaDon.CHO_XAC_NHAN,
            List.of(
                    TrangThaiHoaDon.DA_XAC_NHAN,
                    TrangThaiHoaDon.DA_HUY
            ),

            TrangThaiHoaDon.DA_XAC_NHAN,
            List.of(
                    TrangThaiHoaDon.CHO_VAN_CHUYEN,
                    TrangThaiHoaDon.DA_HUY
            ),

            TrangThaiHoaDon.CHO_VAN_CHUYEN,
            List.of(
                    TrangThaiHoaDon.DANG_GIAO,
                    TrangThaiHoaDon.DA_HUY
            ),

            TrangThaiHoaDon.DANG_GIAO,
            List.of(
                    TrangThaiHoaDon.GIAO_THANH_CONG,
                    TrangThaiHoaDon.GIAO_THAT_BAI
            ),

            TrangThaiHoaDon.GIAO_THAT_BAI,
            List.of(
                    TrangThaiHoaDon.DANG_GIAO,
                    TrangThaiHoaDon.DA_HUY
            ),

            TrangThaiHoaDon.GIAO_THANH_CONG,
            List.of(
                    TrangThaiHoaDon.HOAN_THANH
            ),

            TrangThaiHoaDon.HOAN_THANH,
            List.of(),

            TrangThaiHoaDon.DA_HUY,
            List.of()
    );
}