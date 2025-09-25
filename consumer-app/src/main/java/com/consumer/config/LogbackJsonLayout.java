package com.consumer.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LogbackJsonLayout extends JsonLayout {

   protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {

      map.put("appCode", event.getLoggerContextVO().getName());
      map.put("logtime", String.valueOf(System.currentTimeMillis()));
      IThrowableProxy throwableProxy = event.getThrowableProxy();
      if (throwableProxy != null) {
         try {
            Throwable throwable = ((ThrowableProxy) throwableProxy).getThrowable();
            Map<String, Object> exceptionDetails = new HashMap<>();
            exceptionDetails.put("msg", throwable.getMessage());
            exceptionDetails.put("class", throwable.getClass());
            Throwable rootCause = ExceptionUtils.getRootCause(throwable);
            if (rootCause != null) {
               exceptionDetails.put("class", rootCause.getClass());
            }
            exceptionDetails.put("fullMsg", ExceptionUtils.getMessage(throwable));
            exceptionDetails.put("rcMsg", ExceptionUtils.getRootCauseMessage(throwable));
            map.put("rc", exceptionDetails);
         } catch (Exception ex) {
            log.error("Error in creating Json layout", ex);
         }
      }
   }
}