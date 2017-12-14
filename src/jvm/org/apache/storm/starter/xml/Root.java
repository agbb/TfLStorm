package org.apache.storm.starter.xml;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "headerOrDisruptions" })
@XmlRootElement(name = "Root")
public class Root {

    @XmlElements({ @XmlElement(name = "Header", type = Root.Header.class),
            @XmlElement(name = "Disruptions", type = Root.Disruptions.class) })
    protected List<Object> headerOrDisruptions;

    public List<Object> getHeaderOrDisruptions() {
        if (headerOrDisruptions == null) {
            headerOrDisruptions = new ArrayList<Object>();
        }
        return this.headerOrDisruptions;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "disruption" })
    public static class Disruptions {

        @XmlElement(name = "Disruption")
        protected List<Root.Disruptions.Disruption> disruption;

        public List<Root.Disruptions.Disruption> getDisruption() {
            if (disruption == null) {
                disruption = new ArrayList<Root.Disruptions.Disruption>();
            }
            return this.disruption;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "status", "severity", "levelOfInterest", "category", "subCategory",
                "startTime", "endTime", "location", "corridor", "comments", "currentUpdate", "remarkTime",
                "lastModTime", "causeArea", "recurringSchedule" })
        public static class Disruption implements Serializable {
            private static final long serialVersionUID = 1L;

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
            @XmlElement(name = "CauseArea")
            protected List<Root.Disruptions.Disruption.CauseArea> causeArea;
            @XmlElement(name = "RecurringSchedule")
            protected List<Root.Disruptions.Disruption.RecurringSchedule> recurringSchedule;
            @XmlAttribute(name = "id")
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

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = { "displayPoint", "boundary", "streets" })
            public static class CauseArea implements Serializable {

                private static final long serialVersionUID = 1L;

                @XmlElement(name = "DisplayPoint")
                protected List<Root.Disruptions.Disruption.CauseArea.DisplayPoint> displayPoint;
                @XmlElement(name = "Boundary")
                protected List<Root.Disruptions.Disruption.CauseArea.Boundary> boundary;
                @XmlElement(name = "Streets")
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

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = { "polygon" })
                public static class Boundary implements Serializable {

                    /**
                     * 
                     */
                    private static final long serialVersionUID = 1L;
                    @XmlElement(name = "Polygon")
                    protected List<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon> polygon;

                    public List<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon> getPolygon() {
                        if (polygon == null) {
                            polygon = new ArrayList<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon>();
                        }
                        return this.polygon;
                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = { "coordinatesEN", "coordinatesLL" })
                    public static class Polygon implements Serializable {

                        /**
                         * 
                         */
                        private static final long serialVersionUID = 1L;

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

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = { "point" })
                public static class DisplayPoint implements Serializable {

                    /**
                     * 
                     */
                    private static final long serialVersionUID = 1L;
                    @XmlElement(name = "Point")
                    protected List<Root.Disruptions.Disruption.CauseArea.DisplayPoint.Point> point;

                    public List<Root.Disruptions.Disruption.CauseArea.DisplayPoint.Point> getPoint() {
                        if (point == null) {
                            point = new ArrayList<Root.Disruptions.Disruption.CauseArea.DisplayPoint.Point>();
                        }
                        return this.point;
                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = { "coordinatesEN", "coordinatesLL" })
                    public static class Point implements Serializable{
                        
                        private static final long serialVersionUID = 1L;

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

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = { "street" })
                public static class Streets implements Serializable{

                    /**
                     * 
                     */
                    private static final long serialVersionUID = 1L;
                    @XmlElement(name = "Street")
                    protected List<Root.Disruptions.Disruption.CauseArea.Streets.Street> street;

                    public List<Root.Disruptions.Disruption.CauseArea.Streets.Street> getStreet() {
                        if (street == null) {
                            street = new ArrayList<Root.Disruptions.Disruption.CauseArea.Streets.Street>();
                        }
                        return this.street;
                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = { "name", "closure", "directions", "link" })
                    public static class Street implements Serializable{
                        
                        private static final long serialVersionUID = 1L;
                        protected String name;
                        protected String closure;
                        protected String directions;
                        @XmlElement(name = "Link")
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

                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "", propOrder = { "toid", "line" })
                        public static class Link implements Serializable{
                            
                            private static final long serialVersionUID = 1L;
                            protected String toid;
                            @XmlElement(name = "Line")
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

                            @XmlAccessorType(XmlAccessType.FIELD)
                            @XmlType(name = "", propOrder = { "coordinatesEN", "coordinatesLL" })
                            public static class Line implements Serializable{
                            
                                private static final long serialVersionUID = 1L;
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

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = { "schedule" })
            public static class RecurringSchedule implements Serializable{

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;
                @XmlElement(name = "Schedule")
                protected List<Root.Disruptions.Disruption.RecurringSchedule.Schedule> schedule;

                public List<Root.Disruptions.Disruption.RecurringSchedule.Schedule> getSchedule() {
                    if (schedule == null) {
                        schedule = new ArrayList<Root.Disruptions.Disruption.RecurringSchedule.Schedule>();
                    }
                    return this.schedule;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = { "startTime", "endTime" })
                public static class Schedule implements Serializable{
                            
                    private static final long serialVersionUID = 1L;
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

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "identifier", "displayTitle", "version", "author", "owner", "refreshRate",
            "maxLatency", "timeToError", "schedule", "language", "publishDateTime", "attribution" })
    public static class Header {

        @XmlElement(name = "Identifier")
        protected String identifier;
        @XmlElement(name = "DisplayTitle")
        protected String displayTitle;
        @XmlElement(name = "Version")
        protected String version;
        @XmlElement(name = "Author")
        protected String author;
        @XmlElement(name = "Owner")
        protected String owner;
        @XmlElement(name = "RefreshRate")
        protected String refreshRate;
        @XmlElement(name = "Max_Latency")
        protected String maxLatency;
        @XmlElement(name = "TimeToError")
        protected String timeToError;
        @XmlElement(name = "Schedule")
        protected String schedule;
        @XmlElement(name = "Language")
        protected String language;
        @XmlElement(name = "PublishDateTime", nillable = true)
        protected List<Root.Header.PublishDateTime> publishDateTime;
        @XmlElement(name = "Attribution")
        protected List<Root.Header.Attribution> attribution;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String value) {
            this.identifier = value;
        }

        public String getDisplayTitle() {
            return displayTitle;
        }

        public void setDisplayTitle(String value) {
            this.displayTitle = value;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String value) {
            this.version = value;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String value) {
            this.author = value;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String value) {
            this.owner = value;
        }

        public String getRefreshRate() {
            return refreshRate;
        }

        public void setRefreshRate(String value) {
            this.refreshRate = value;
        }

        public String getMaxLatency() {
            return maxLatency;
        }

        public void setMaxLatency(String value) {
            this.maxLatency = value;
        }

        public String getTimeToError() {
            return timeToError;
        }

        public void setTimeToError(String value) {
            this.timeToError = value;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String value) {
            this.schedule = value;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String value) {
            this.language = value;
        }

        public List<Root.Header.PublishDateTime> getPublishDateTime() {
            if (publishDateTime == null) {
                publishDateTime = new ArrayList<Root.Header.PublishDateTime>();
            }
            return this.publishDateTime;
        }

        public List<Root.Header.Attribution> getAttribution() {
            if (attribution == null) {
                attribution = new ArrayList<Root.Header.Attribution>();
            }
            return this.attribution;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "url", "text", "logo" })
        public static class Attribution {

            @XmlElement(name = "Url")
            protected String url;
            @XmlElement(name = "Text")
            protected String text;
            @XmlElement(name = "Logo")
            protected String logo;

            public String getUrl() {
                return url;
            }

            public void setUrl(String value) {
                this.url = value;
            }

            public String getText() {
                return text;
            }

            public void setText(String value) {
                this.text = value;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String value) {
                this.logo = value;
            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "value" })
        public static class PublishDateTime {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "canonical")
            protected String canonical;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getCanonical() {
                return canonical;
            }

            public void setCanonical(String value) {
                this.canonical = value;
            }

        }

    }

}
