/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.docs.richfaces.demo.chart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * @author jjamrich
 *
 */
public class PieChart {

		private static final long serialVersionUID = 1L;
		
		private JFreeChart chart;

		public PieChart(String chartTitle, Map<String, Number> data) {			 
	        createChart(createDataset(data), chartTitle);	       
	    }
		
		public PieChart(String chartTitle, List<Object[]> lo) {			 
	        createChart(createDataset(lo), chartTitle);	       
	    }
	    
	    /**
	     * Creates a sample dataset 
	     */
	    private  PieDataset createDataset(Map<String, Number> data) {
	        DefaultPieDataset result = new DefaultPieDataset();
	        
	        Iterator<String> it = data.keySet().iterator();
	        while(it.hasNext()) {
	        	String key = it.next();
	        	result.setValue(key, data.get(key));
	        }
	        
	        return result;	        
	    }
	    
	    /**
	     * Creates a sample dataset 
	     */
	    private  PieDataset createDataset(List<Object[]> lo) {
	        DefaultPieDataset result = new DefaultPieDataset();
	        
	        Iterator<Object[]> it = lo.iterator();
	        while(it.hasNext()) {
	        	Object[] row = (Object[])it.next();
	        	result.setValue((String)row[0]/* Phone number, selected property */, 
	        			(Integer)row[1] /* count(*) on groub by column, 
	        			result of aggregate function: number */);
	        }
	        
	        return result;	        
	    }
	    
	    /**
	     * Creates a chart
	     */
	    private JFreeChart createChart(PieDataset dataset, String title) {
	        
	        chart = ChartFactory.createPieChart3D(
	            title,  				// chart title
	            dataset,                // data
	            true,                   // include legend
	            true,
	            false
	        );

	        PiePlot3D plot = (PiePlot3D) chart.getPlot();
	        plot.setStartAngle(290);
	        plot.setDirection(Rotation.CLOCKWISE);
	        plot.setForegroundAlpha(0.8f);
	        return chart;
	        
	    }
	    
	    public void writeChartAsPng(OutputStream out) throws IOException {
	    	ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
	    }
}
