package br.com.fiap.agroorbit.config;

import br.com.fiap.agroorbit.models.CropArea;
import br.com.fiap.agroorbit.models.Farm;
import br.com.fiap.agroorbit.models.SatelliteData;
import br.com.fiap.agroorbit.models.Sensor;
import br.com.fiap.agroorbit.models.SensorReading;
import br.com.fiap.agroorbit.models.User;
import br.com.fiap.agroorbit.models.WeatherStationSensor;
import br.com.fiap.agroorbit.models.embedded.GeoLocation;
import br.com.fiap.agroorbit.models.enums.AreaUnit;
import br.com.fiap.agroorbit.models.enums.CropAreaStatus;
import br.com.fiap.agroorbit.models.enums.SatelliteSource;
import br.com.fiap.agroorbit.models.enums.SensorStatus;
import br.com.fiap.agroorbit.models.enums.UserRole;
import br.com.fiap.agroorbit.repositories.CropAreaRepository;
import br.com.fiap.agroorbit.repositories.FarmRepository;
import br.com.fiap.agroorbit.repositories.SatelliteDataRepository;
import br.com.fiap.agroorbit.repositories.SensorReadingRepository;
import br.com.fiap.agroorbit.repositories.SensorRepository;
import br.com.fiap.agroorbit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final CropAreaRepository cropAreaRepository;
    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final SatelliteDataRepository satelliteDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User producer = User.builder()
                .name("Lucas Viana")
                .email("lucas@agroorbit.com")
                .password(passwordEncoder.encode("123456"))
                .role(UserRole.ROLE_PRODUCER)
                .build();

        userRepository.save(producer);

        Farm farm = Farm.builder()
                .user(producer)
                .name("Fazenda Sol Nascente")
                .owner("Lucas Viana")
                .city("São Paulo")
                .state("SP")
                .country("Brasil")
                .build();

        farmRepository.save(farm);

        String boundaryGeoJson = """
                {
                  "type": "Polygon",
                  "coordinates": [
                    [
                      [-46.634000, -23.551000],
                      [-46.632000, -23.551000],
                      [-46.632000, -23.549000],
                      [-46.634000, -23.549000],
                      [-46.634000, -23.551000]
                    ]
                  ]
                }
                """;

        CropArea cropArea = CropArea.builder()
                .farm(farm)
                .name("Talhão A")
                .cropType("Milho")
                .areaSize(new BigDecimal("4.50"))
                .areaUnit(AreaUnit.HA)
                .location(new GeoLocation(new BigDecimal("-23.550520"), new BigDecimal("-46.633308")))
                .boundaryGeoJson(boundaryGeoJson)
                .status(CropAreaStatus.NORMAL)
                .build();

        cropAreaRepository.save(cropArea);

        Sensor sensor = new WeatherStationSensor(
                cropArea,
                "Estação Agrícola Simulada",
                SensorStatus.ACTIVE,
                LocalDate.now()
        );

        sensorRepository.save(sensor);

        SensorReading sensorReading = SensorReading.builder()
                .sensor(sensor)
                .temperature(new BigDecimal("35.20"))
                .airHumidity(new BigDecimal("27.50"))
                .soilHumidity(new BigDecimal("22.00"))
                .manualAlert("N")
                .build();

        sensorReadingRepository.save(sensorReading);

        SatelliteData satelliteData = SatelliteData.builder()
                .cropArea(cropArea)
                .source(SatelliteSource.SENTINEL_HUB_STATISTICAL_API)
                .ndviAverage(new BigDecimal("0.3800"))
                .ndviMin(new BigDecimal("0.2500"))
                .ndviMax(new BigDecimal("0.6100"))
                .surfaceTemperature(new BigDecimal("36.50"))
                .cloudCoverage(new BigDecimal("18.00"))
                .captureDate(LocalDate.now())
                .build();

        satelliteDataRepository.save(satelliteData);
    }
}