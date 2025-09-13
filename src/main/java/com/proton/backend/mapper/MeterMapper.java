package com.proton.backend.mapper;

import com.proton.backend.dto.MeterDTO;
import com.proton.backend.model.Meter;
import org.apache.catalina.mapper.Mapper;

public class MeterMapper {

    public static Meter fromMeterDTO(MeterDTO meterDTO) {
        Meter meter = new Meter();
        meter.setId(meterDTO.getId());
        meter.setUser(meterDTO.getUser());
        meter.setCurrent(meterDTO.getCurrent());
        meter.setVoltage(meterDTO.getVoltage());
        meter.setSerialNumber(meterDTO.getSerialNumber());
        meter.setPowerFactor(meterDTO.getPowerFactor());
        meter.setPower(meterDTO.getPower());
        return meter;
    }

    public static MeterDTO toMeterDTO(Meter meter) {
        MeterDTO meterDTO = new MeterDTO();
        meterDTO.setId(meter.getId());
        meterDTO.setUser(meter.getUser());
        meterDTO.setCurrent(meter.getCurrent());
        meterDTO.setVoltage(meter.getVoltage());
        meterDTO.setSerialNumber(meter.getSerialNumber());
        meterDTO.setPowerFactor(meter.getPowerFactor());
        meterDTO.setPower(meter.getPower());
        return meterDTO;
    }
}
