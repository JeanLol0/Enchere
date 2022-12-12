/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

//package cz.cuni.mff.skychart.ui.control;

import com.sun.javafx.scene.control.DatePickerContent;

import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;


import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.AccessibleRole;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.ColumnConstraints;

/**
 * A date time picker component.
 *
 * @author Peter Grajcar
 */
public class DateTimePicker extends GridPane {


	private LocalDateTime localDateTime;
	private DatePicker fakeDatePicker;
	
	private Label hour;
	private Slider hourSlider;
	private Label minute;
	private Slider minuteSlider;
	private Label second;
	private Slider secondSlider;

	public DateTimePicker() {
		this("DateTimePicker");
	}

	public DateTimePicker(String name) {
		this(FXCollections.observableArrayList(), name);
	}

	public DateTimePicker(ObservableList<LocalDateTime> items, String name) {
//		super(items);
//		this.setEditable(true);
this.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
		setAccessibleRole(AccessibleRole.DATE_PICKER);
		getStyleClass().add("date-picker");

		hour = new Label("00");
		hour.setPadding(new Insets(0, 0, 0, 10));
		hourSlider = new Slider(0, 23, 0);
		hourSlider.setPadding(new Insets(5, 0, 5, 0));
		minute = new Label("00");
		minute.setPadding(new Insets(0, 0, 0, 10));
		minuteSlider = new Slider(0, 59, 0);
		minuteSlider.setPadding(new Insets(0, 0, 5, 0));
		second = new Label("00");
		second.setPadding(new Insets(0, 0, 0, 10));
		secondSlider = new Slider(0, 59, 0);
		secondSlider.setPadding(new Insets(0, 0, 5, 0));
		localDateTime = LocalDateTime.now();


//		this.setConverter(new StringConverter<>() {
//			@Override
//			public String toString(LocalDateTime localDateTime) {
//				if (localDateTime == null) {
//					return null;
//				}
//				DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM);
//				return localDateTime.format(formatter);
//			}
//
//			@Override
//			public LocalDateTime fromString(String s) {
//				try {
//					DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM);
//					return LocalDateTime.parse(s, formatter);
//				} catch (DateTimeParseException exception) {
//					try {
//						return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(s) * 1000), TimeZone.getDefault().toZoneId());
//					} catch (NumberFormatException ex) {
//						return LocalDateTime.now();
//					}
//				}
//			}
//		});


		fakeDatePicker = new DatePicker();
		fakeDatePicker.valueProperty().setValue(localDateTime.toLocalDate());
		fakeDatePicker.valueProperty().addListener(this::setValue);
		DatePickerSkin datePickerSkin = new DatePickerSkin(fakeDatePicker);
//		fakeDatePicker.setSkin(datePickerSkin);
		


//		GridPane gridPane = new GridPane();
		ColumnConstraints columnConstraints1 = new ColumnConstraints();
		columnConstraints1.setMinWidth(30);
		ColumnConstraints columnConstraints2 = new ColumnConstraints();
		this.getColumnConstraints().addAll(columnConstraints1, columnConstraints2);
                setValue();
		this.add(fakeDatePicker, 0, 0, 2, 1);


		hour.textProperty().bind(hourSlider.valueProperty().asString("%.0f"));
		hourSlider.valueProperty().addListener(this::setValue);

		this.add(hour, 0, 1);
		this.add(hourSlider, 1, 1);


		minute.textProperty().bind(minuteSlider.valueProperty().asString("%.0f"));
		minuteSlider.valueProperty().addListener(this::setValue);
		this.add(minute, 0, 2);
		this.add(minuteSlider, 1, 2);

		second.textProperty().bind(secondSlider.valueProperty().asString("%.0f"));
		secondSlider.valueProperty().addListener(this::setValue);
		this.add(second, 0, 3);
		this.add(secondSlider, 1, 3);

		
	}




	private void setValue() {
		int hours = (int) Math.round(hourSlider.getValue());
		int minutes = (int) Math.round(minuteSlider.getValue());
		int seconds = (int) Math.round(secondSlider.getValue());
		localDateTime = LocalDateTime.of(fakeDatePicker.getValue(), LocalTime.of(hours, minutes, seconds));

//		DateTimePicker.this.valueProperty().setValue(localDateTime);
	}

	public void setDate(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
		hourSlider.setValue(localDateTime.getHour());
		minuteSlider.setValue(localDateTime.getMinute());
		secondSlider.setValue(localDateTime.getSecond());
		fakeDatePicker.setValue(localDateTime.toLocalDate());
//		DateTimePicker.this.valueProperty().setValue(localDateTime);
	}

	public void setDate(LocalDate localDate) {
		this.localDateTime = localDate.atTime(0, 0);
		setDate(localDateTime);
	}



	private void setValue(Observable observable) {
		setValue();
	}

//	public long getSecondsTimestamp() {
//		return Timestamp.valueOf(this.getValue()).getTime() / 1000;
//	}
}

//public class DateTimePicker extends GridPane {
//
//    
//    private DatePicker datePicker;
//
//    public static final String DEFAULT_FORMAT = "dd.MM.yyyy HH:mm:ss";
//
////    private final static Logger logger = LogManager.getLogger(DateTimePicker.class);
//
//    private DateTimeFormatter formatter;
//    private ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(LocalDateTime.now());
//    private ObjectProperty<String> format = new SimpleObjectProperty<String>() {
//        public void set(String newValue) {
//            super.set(newValue);
//            formatter = DateTimeFormatter.ofPattern(newValue);
//        }
//    };
//
//    private ChangeListener<LocalDateTime> onDateTimeChangedListener;
//
//    public DateTimePicker() throws IOException {
//        ResourceBundle localisation = Localisation.getBundle();
//        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/control/DateTimePicker.fxml"), localisation);
////        loader.setRoot(this);
////        loader.setController(this);
////        loader.load();
//
//        getStyleClass().add("datetime-picker");
//        setFormat(DEFAULT_FORMAT);
//        datePicker.setConverter(new InternalConverter());
//
//        // Syncronize changes to the underlying date value back to the dateTimeValue
//        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == null) {
//                dateTimeValue.set(null);
//            } else {
//                if (dateTimeValue.get() == null) {
//                    dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
//                } else {
//                    LocalTime time = dateTimeValue.get().toLocalTime();
//                    dateTimeValue.set(LocalDateTime.of(newValue, time));
//                }
//            }
//            if(onDateTimeChangedListener != null)
//                onDateTimeChangedListener.changed(dateTimeValue, dateTimeValue.get(), dateTimeValue.get());
//        });
//
//        // Syncronize changes to dateTimeValue back to the underlying date value
//        dateTimeValue.addListener((observable, oldValue, newValue) -> {
//            datePicker.setValue(newValue == null ? null : newValue.toLocalDate());
//            datePicker.getEditor().setText(getDateTimeValue().format(formatter));
//            if(onDateTimeChangedListener != null)
//                onDateTimeChangedListener.changed(dateTimeValue, dateTimeValue.get(), dateTimeValue.get());
//        });
//
//        // Persist changes onblur
//        datePicker.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue)
//                simulateEnterPressed();
//        });
//
//    }
//
//    public void setOnDateTimeChangedListener(ChangeListener<LocalDateTime> onDateTimeChangedListener) {
//        this.onDateTimeChangedListener = onDateTimeChangedListener;
//    }
//
//    private void simulateEnterPressed() {
//        datePicker.getEditor().fireEvent(new KeyEvent(datePicker.getEditor(), datePicker.getEditor(), KeyEvent.KEY_PRESSED, null, null, KeyCode.ENTER, false, false, false, false));
//    }
//
//    public LocalDateTime getDateTimeValue() {
//        return dateTimeValue.get();
//    }
//
//    public void setDateTimeValue(LocalDateTime dateTimeValue) {
//        this.dateTimeValue.set(dateTimeValue);
//    }
//
//    public ObjectProperty<LocalDateTime> dateTimeValueProperty() {
//        return dateTimeValue;
//    }
//
//    public String getFormat() {
//        return format.get();
//    }
//
//    public ObjectProperty<String> formatProperty() {
//        return format;
//    }
//
//    public void setFormat(String format) {
//        this.format.set(format);
//    }
//
//    class InternalConverter extends StringConverter<LocalDate> {
//        public String toString(LocalDate object) {
//            LocalDateTime value = getDateTimeValue();
//            return (value != null) ? value.format(formatter) : "";
//        }
//
//        public LocalDate fromString(String value) {
//            if (value == null) {
//                dateTimeValue.set(null);
//                return null;
//            }
//
//            dateTimeValue.set(LocalDateTime.parse(value, formatter));
//            return dateTimeValue.get().toLocalDate();
//        }
//    }
//}