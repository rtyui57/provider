package com.ramon.provider.rs.converter;

import com.ramon.provider.model.DeviceRegister;
import com.ramon.provider.rs.entity.RSDeviceRegister;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeviceRegisterConverter {

    public DeviceRegister convert(RSDeviceRegister source) {
        DeviceRegister deviceRegister = new DeviceRegister();
        deviceRegister.setCoordenadas(source.getCoordenadas());
        deviceRegister.setPresion(source.getPresion());
        deviceRegister.setOxigenoSangre(source.getOxigenoSangre());
        deviceRegister.setFechaRegistro(new Date(source.getTimestamp()));
        deviceRegister.setKilometrosAcumulados(source.getKilometrosAcumulados());
        deviceRegister.setRitmoCardiaco(source.getRitmoCardiaco());
        deviceRegister.setTemperaturaAmbiente(source.getTemperaturaAmbiente());
        deviceRegister.setTemperaturaCorporal(source.getTemperaturaCorporal());
        return deviceRegister;
    }
}
