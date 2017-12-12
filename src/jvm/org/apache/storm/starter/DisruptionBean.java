package org.apache.storm.starter;

import java.util.ArrayList;
import java.util.List;
import org.apache.storm.starter.xml.*;



public class DisruptionBean extends Root.Disruptions.Disruption{

	protected String status;
	protected String severity;
	protected String levelOfInterest;
	protected String category;
	protected String subCategory;
	protected String startTime;
	protected String endTime;
	protected String location;
	protected String corridor;
	protected String comments;
	protected String currentUpdate;
	protected String remarkTime;
	protected String lastModTime;

	protected List<Root.Disruptions.Disruption.CauseArea> causeArea;
	protected List<Root.Disruptions.Disruption.RecurringSchedule> recurringSchedule;
	protected String id;

	public String getStatus() {
		return status;
	}

	public void setStatus(String value) {
		this.status = value;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String value) {
		this.severity = value;
	}

	public String getLevelOfInterest() {
		return levelOfInterest;
	}

	public void setLevelOfInterest(String value) {
		this.levelOfInterest = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String value) {
		this.category = value;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String value) {
		this.subCategory = value;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String value) {
		this.startTime = value;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String value) {
		this.endTime = value;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String value) {
		this.location = value;
	}

	public String getCorridor() {
		return corridor;
	}

	public void setCorridor(String value) {
		this.corridor = value;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String value) {
		this.comments = value;
	}

	public String getCurrentUpdate() {
		return currentUpdate;
	}

	public void setCurrentUpdate(String value) {
		this.currentUpdate = value;
	}

	public String getRemarkTime() {
		return remarkTime;
	}

	public void setRemarkTime(String value) {
		this.remarkTime = value;
	}

	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String value) {
		this.lastModTime = value;
	}

	public List<Root.Disruptions.Disruption.CauseArea> getCauseArea() {
		if (causeArea == null) {
			causeArea = new ArrayList<Root.Disruptions.Disruption.CauseArea>();
		}
		return this.causeArea;
	}

	public List<Root.Disruptions.Disruption.RecurringSchedule> getRecurringSchedule() {
		if (recurringSchedule == null) {
			recurringSchedule = new ArrayList<Root.Disruptions.Disruption.RecurringSchedule>();
		}
		return this.recurringSchedule;
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public static class CauseArea {

		protected List<Root.Disruptions.Disruption.CauseArea.DisplayPoint> displayPoint;

		protected List<Root.Disruptions.Disruption.CauseArea.Boundary> boundary;

		protected List<Root.Disruptions.Disruption.CauseArea.Streets> streets;

		public List<Root.Disruptions.Disruption.CauseArea.DisplayPoint> getDisplayPoint() {
			if (displayPoint == null) {
				displayPoint = new ArrayList<Root.Disruptions.Disruption.CauseArea.DisplayPoint>();
			}
			return this.displayPoint;
		}

		public List<Root.Disruptions.Disruption.CauseArea.Boundary> getBoundary() {
			if (boundary == null) {
				boundary = new ArrayList<Root.Disruptions.Disruption.CauseArea.Boundary>();
			}
			return this.boundary;
		}

		public List<Root.Disruptions.Disruption.CauseArea.Streets> getStreets() {
			if (streets == null) {
				streets = new ArrayList<Root.Disruptions.Disruption.CauseArea.Streets>();
			}
			return this.streets;
		}

		public static class Boundary {

			protected List<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon> polygon;

			public List<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon> getPolygon() {
				if (polygon == null) {
					polygon = new ArrayList<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon>();
				}
				return this.polygon;
			}

			public static class Polygon {

				protected String coordinatesEN;
				protected String coordinatesLL;

				public String getCoordinatesEN() {
					return coordinatesEN;
				}

				public void setCoordinatesEN(String value) {
					this.coordinatesEN = value;
				}

				public String getCoordinatesLL() {
					return coordinatesLL;
				}

				public void setCoordinatesLL(String value) {
					this.coordinatesLL = value;
				}

			}

		}

		public static class DisplayPoint {

			protected List<Root.Disruptions.Disruption.CauseArea.DisplayPoint.Point> point;

			public List<Root.Disruptions.Disruption.CauseArea.DisplayPoint.Point> getPoint() {
				if (point == null) {
					point = new ArrayList<Root.Disruptions.Disruption.CauseArea.DisplayPoint.Point>();
				}
				return this.point;
			}

			public static class Point {

				protected String coordinatesEN;
				protected String coordinatesLL;

				public String getCoordinatesEN() {
					return coordinatesEN;
				}

				public void setCoordinatesEN(String value) {
					this.coordinatesEN = value;
				}

				public String getCoordinatesLL() {
					return coordinatesLL;
				}

				public void setCoordinatesLL(String value) {
					this.coordinatesLL = value;
				}

			}

		}

		public static class Streets {

			protected List<Root.Disruptions.Disruption.CauseArea.Streets.Street> street;

			public List<Root.Disruptions.Disruption.CauseArea.Streets.Street> getStreet() {
				if (street == null) {
					street = new ArrayList<Root.Disruptions.Disruption.CauseArea.Streets.Street>();
				}
				return this.street;
			}

			public static class Street {

				protected String name;
				protected String closure;
				protected String directions;
				protected List<Root.Disruptions.Disruption.CauseArea.Streets.Street.Link> link;

				public String getName() {
					return name;
				}

				public void setName(String value) {
					this.name = value;
				}

				public String getClosure() {
					return closure;
				}

				public void setClosure(String value) {
					this.closure = value;
				}

				public String getDirections() {
					return directions;
				}

				public void setDirections(String value) {
					this.directions = value;
				}

				public List<Root.Disruptions.Disruption.CauseArea.Streets.Street.Link> getLink() {
					if (link == null) {
						link = new ArrayList<Root.Disruptions.Disruption.CauseArea.Streets.Street.Link>();
					}
					return this.link;
				}

				public static class Link {

					protected String toid;
					protected List<Root.Disruptions.Disruption.CauseArea.Streets.Street.Link.Line> line;

					public String getToid() {
						return toid;
					}

					public void setToid(String value) {
						this.toid = value;
					}

					public List<Root.Disruptions.Disruption.CauseArea.Streets.Street.Link.Line> getLine() {
						if (line == null) {
							line = new ArrayList<Root.Disruptions.Disruption.CauseArea.Streets.Street.Link.Line>();
						}
						return this.line;
					}

					public static class Line {

						protected String coordinatesEN;
						protected String coordinatesLL;

						public String getCoordinatesEN() {
							return coordinatesEN;
						}

						public void setCoordinatesEN(String value) {
							this.coordinatesEN = value;
						}

						public String getCoordinatesLL() {
							return coordinatesLL;
						}

						public void setCoordinatesLL(String value) {
							this.coordinatesLL = value;
						}

					}

				}

			}

		}

	}

	public static class RecurringSchedule {

		protected List<Root.Disruptions.Disruption.RecurringSchedule.Schedule> schedule;

		public List<Root.Disruptions.Disruption.RecurringSchedule.Schedule> getSchedule() {
			if (schedule == null) {
				schedule = new ArrayList<Root.Disruptions.Disruption.RecurringSchedule.Schedule>();
			}
			return this.schedule;
		}

		public static class Schedule {

			protected String startTime;
			protected String endTime;

			public String getStartTime() {
				return startTime;
			}

			public void setStartTime(String value) {
				this.startTime = value;
			}

			public String getEndTime() {
				return endTime;
			}

			public void setEndTime(String value) {
				this.endTime = value;
			}

		}

	}

}