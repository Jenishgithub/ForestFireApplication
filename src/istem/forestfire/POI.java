package istem.forestfire;

import org.mapsforge.core.model.LatLong;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class POI implements Comparable<POI> {
	private String forest_name;
	private double lat;
	private double lng;
	private String forest_fire_type;
	private LatLong latlong;
	private String id;
	private String fire_date;
	private String affect_people;
	private String affect_house;
	private String area;
	private String cause;
	private String regeneration;
	private String fire_size;
	private String fire_status;
	private String forest_density;
	private String how_extinguished;
	private String burnt_area_feature;
	private String other_institutions;
	private String no_of_trees_burnt;
	private String distance_from_fire;
	private String fire_extinguish_time;
	private String cfug_arrival_time;
	private String impact_in_water;
	private String fire_start_from;
	private String area_damaged;
	private String other_remarks;
	private String how_not_extinguished;
	private String damage_of_ntft;
	private String lesson_for_future;
	private String use_of_water;
	private String damage_of_wildlife;
	private String fire_time;
	private String no_cfug_people_mobilized;
	private String triggers;
	private String equipments_brought;
	private String impact_in_soil;

	public POI(double lats, double lgts, String forest_name) {
		this.lat = lats;
		this.lng = lgts;
		this.forest_name = forest_name;
	}

	public POI(double lats, double lgts, String forest_name,
			String forest_fire_type) {
		this.lat = lats;
		this.lng = lgts;
		this.forest_name = forest_name;
		this.forest_fire_type = forest_fire_type;
	}

	public POI(double lats, double lgts, String name, String forest_fire_type,
			String id) {
		this.lat = lats;
		this.lng = lgts;
		this.forest_name = name;
		this.forest_fire_type = forest_fire_type;
		this.id = id;
	}

	public double getLat() {
		return this.lat;
	}

	public double getLng() {
		return this.lng;
	}

	public String getId() {
		return this.id;
	}

	public LatLong getLatLng() {
		this.latlong = new LatLong(this.lat, this.lng);
		return latlong;
	}

	public String getForestName() {
		return this.forest_name;
	}

	public String getforest_fire_type() {
		return this.forest_fire_type;
	}

	public void setLat(double paramDouble) {
		this.lat = paramDouble;
	}

	public void setLng(double paramDouble) {
		this.lng = paramDouble;
	}

	public void setforestName(String paramString) {
		this.forest_name = paramString;
	}

	public void setforest_fire_type(String paramString) {
		this.forest_fire_type = paramString;
	}

	public void setId(String paramString) {
		this.id = paramString;
	}

	public void setForestFireDate(String date) {
		this.fire_date = date;
	}

	public void setNoOfAffectedPeoples(String no) {
		this.affect_people = no;
	}

	public void setNoOfAffectedHouses(String no) {
		this.affect_house = no;
	}

	public void setArea(String no) {
		this.area = no;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public void setRegeneration(String regeneration) {
		this.regeneration = regeneration;
	}

	public void setFireSize(String firesize) {
		this.fire_size = firesize;
	}

	public void setStatus(String status) {
		this.fire_status = status;
	}

	public void setDensity(String density) {
		this.forest_density = density;
	}

	// //////////After

	public void setHowExtinguished(String how_extinguished) {
		this.how_extinguished = how_extinguished;
	}

	public void setBurntAreaFeature(String burnt_area_feature) {
		this.burnt_area_feature = burnt_area_feature;
	}

	public void setOtherInstitutions(String other_institutions) {
		this.other_institutions = other_institutions;
	}

	public void setNoOfTreesBurnt(String no_of_trees_burnt) {
		this.no_of_trees_burnt = no_of_trees_burnt;
	}

	public void setDistanceFromFire(String distance_from_fire) {
		this.distance_from_fire = distance_from_fire;
	}

	public void setFireExtinguishTime(String fire_extinguish_time) {
		this.fire_extinguish_time = fire_extinguish_time;
	}

	public void setCfugArrivalTime(String cfug_arrival_time) {
		this.cfug_arrival_time = cfug_arrival_time;
	}

	public void setImpactInWater(String impact_in_water) {
		this.impact_in_water = impact_in_water;
	}

	public void setFireStartFrom(String fire_start_from) {
		this.fire_start_from = fire_start_from;
	}

	public void setAreaDamaged(String area_damaged) {
		this.area_damaged = area_damaged;
	}

	public void setOtherRemarks(String other_remarks) {
		this.other_remarks = other_remarks;
	}

	public void setHowNotExtinguished(String how_not_extinguished) {
		this.how_not_extinguished = how_not_extinguished;
	}

	public void setdamageOfNtft(String damage_of_ntft) {
		this.damage_of_ntft = damage_of_ntft;
	}

	public void setLessonForFuture(String lesson_for_future) {
		this.lesson_for_future = lesson_for_future;
	}

	public void setUseOfWater(String use_of_water) {
		this.use_of_water = use_of_water;
	}

	public void setDamageOfWildLife(String damage_of_wildlife) {
		this.damage_of_wildlife = damage_of_wildlife;
	}

	public void setFireTime(String fire_time) {
		this.fire_time = fire_time;
	}

	public void setNoCFUGPeopelMobilized(String no_cfug_people_mobilized) {
		this.no_cfug_people_mobilized = no_cfug_people_mobilized;
	}

	public void setTriggers(String triggers) {
		this.triggers = triggers;
	}

	public void setimpactInSoil(String impact_in_soil) {
		this.impact_in_soil = impact_in_soil;
	}

	public void setEquipmentsBrought(String equipments_brought) {
		this.equipments_brought = equipments_brought;
	}

	public String getHowExtinguished() {
		return how_extinguished;
	}

	public String getBurntAreaFeature() {
		return burnt_area_feature;
	}

	public String getOtherInstitutions() {
		return other_institutions;
	}

	public String getNoOfTreesBurnt() {
		return no_of_trees_burnt;
	}

	public String getDistanceFromFire() {
		return distance_from_fire;
	}

	public String getFireExtinguishTime() {
		return fire_extinguish_time;
	}

	public String getCfugArrivalTime() {
		return cfug_arrival_time;
	}

	public String getImpactInWater() {
		return impact_in_water;
	}

	public String getFireStartFrom() {
		return fire_start_from;
	}

	public String getAreaDamaged() {
		return area_damaged;
	}

	public String getOtherRemarks() {
		return other_remarks;
	}

	public String getHowNotExtinguished() {
		return how_not_extinguished;
	}

	public String getdamageOfNtft() {
		return damage_of_ntft;
	}

	public String getLessonForFuture() {
		return lesson_for_future;
	}

	public String getUseOfWater() {
		return use_of_water;
	}

	public String getDamageOfWildLife() {
		return damage_of_wildlife;
	}

	public String getFireTime() {
		return fire_time;
	}

	public String getNoCFUGPeopelMobilized() {
		return no_cfug_people_mobilized;
	}

	public String getTriggers() {
		return triggers;
	}

	public String getimpactInSoil() {
		return impact_in_soil;
	}

	public String getEquipmentsBrought() {
		return equipments_brought;
	}

	public String getForestFireDate() {
		return this.fire_date;
	}

	public Date getFireDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Date date1 = null;
		if (!fire_date.equals("null")) {
			try {
				date1 = formatter.parse(this.fire_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date1;
	}

	public String getNoOfAffectedPeoples() {
		return this.affect_people;
	}

	public String getNoOfAffectedHouses() {
		return this.affect_house;
	}

	public String getArea() {
		return this.area;
	}

	public String getCause() {
		return this.cause;
	}

	public String getRegeneration() {
		return this.regeneration;
	}

	public String getFireSize() {
		return this.fire_size;
	}

	public String getStatus() {
		return this.fire_status;
	}

	public String getDensity() {
		return this.forest_density;
	}

	@Override
	public int compareTo(POI poi) {
		int compareage=Integer.parseInt(((POI)poi).getId());
        /* For Ascending order*/
        //return Integer.parseInt(this.id)-compareage;

        /* For Descending order do like this */
        return compareage-Integer.parseInt(this.id);
	}
}