package cz.vsimanek.closeit.test.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.springframework.stereotype.Component;

/**
 *
 * @author Vlada Simanek
 */
@Component
public class FlightDelayServiceImpl implements FlightDelayService {

    // 15 	ArrDelay 	arrival delay, in minutes
    private static final int ARRIVAL_DELAY_INDEX = 14;
    // 18 	Dest 	destination IATA airport code
    private static final int DESTINATION_CODE_INDEX = 17;
    // 22 	Cancelled 	was the flight cancelled?
    private static final int CANCELLED_INDEX = 21;
    private static final String CANCELLED_FALSE = "0";
    // Los Angeles airport code
    private static final String LA_AIRPORT_CODE = "LAX";
    // uknown value
    private static final String NA_VALUE = "NA";
    private static final String SPLIT_CHAR = ",";
    // url for data download
    private static final String DATA_URL = "http://stat-computing.org/dataexpo/2009/1989.csv.bz2";
    // cache
    private Double summary = 0d;

    @Override
    public Double averageFlightsDelayIn1989() throws IOException {
        if (summary > 0) {
            return summary;
        }
        URL website = new URL(DATA_URL);

        InputStream inputFS = website.openStream();
        
        BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(inputFS);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(bzIn))) {
            // skip the header of the csv
            summary = br.lines().skip(1).filter(line -> {
                String[] p = line.split(SPLIT_CHAR);
                return p[DESTINATION_CODE_INDEX].equals(LA_AIRPORT_CODE)
                        && p[CANCELLED_INDEX].equals(CANCELLED_FALSE)
                        && !p[ARRIVAL_DELAY_INDEX].equals(NA_VALUE)
                        && Integer.valueOf(p[ARRIVAL_DELAY_INDEX]) > -1;
            }).mapToDouble(line -> {
                String[] p = line.split(",");
                return Double.valueOf(p[ARRIVAL_DELAY_INDEX]);
            }).average().orElse(0d);
            return summary;
        }
    }

}
