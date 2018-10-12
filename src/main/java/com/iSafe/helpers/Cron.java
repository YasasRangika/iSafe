package com.iSafe.helpers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iSafe.entities.Accident;
import com.iSafe.models.RecordDto;
import com.iSafe.repositories.AccidentRepository;
import com.iSafe.repositories.BlackSpotRepository;
import com.iSafe.services.RecordService;

//This is the cron job initialization class
//Blackspots will be generated via algorithm in every week Sunday mid night

@Component
public class Cron {

	@Autowired
	BlackSpotRepository blackSpotRepository;
	@Autowired
	AccidentRepository accidentRepository;
	@Autowired
	RecordService recordService;

//	@Scheduled(fixedRate=2000)
	@Scheduled(cron = "0 0 0 * * SUN") // every Sunday at midnight
	public void cron() throws ParseException {

		RecordDto blackSpot = new RecordDto();
		List<Accident> list = accidentRepository.findAllByLatLan();
//		List<BlackSpot> blackList = new ArrayList<>();

//		int index = 0;
		for (Accident a : list) {
			System.out.println(a.getAccident());
			List<Accident> accList = new ArrayList<>();
			if (blackSpotRepository.findBlackSpot(a.getLat(), a.getLng()) == null) {
				accList = accidentRepository.findByLatLan(a.getLat(), a.getLng());
			}
			if (accList.size() >= 3) {
				blackSpot.setLatitude(a.getLat());
				blackSpot.setLongitude(a.getLng());
				blackSpot.setRadius(0.5);
				blackSpot.setMessage("රිය අනතුරු සුලභ ප්‍රදේශයකි!!!");
				recordService.recordBlackSpot(blackSpot);
			}

//			index ++;
		}
	}
}
