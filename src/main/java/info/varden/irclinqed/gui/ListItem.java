/*
   Copyright 2014 Marius Lindvall

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package info.varden.irclinqed.gui;

public class ListItem implements Comparable {
	public String title;
	public String line1;
	public String line2;
	
	public Object metaData;
	
	public int titleColor;
	public int line1Color;
	public int line2Color;
	
	public ListItem(String title, String line1, String line2) {
		this(null, title, 16777215, line1, 8421504, line2, 8421504);
	}
	
	public ListItem(Object metaData, String title, String line1, String line2) {
		this(metaData, title, 16777215, line1, 8421504, line2, 8421504);
	}
	
	public ListItem(String title, int titleColor, String line1, int line1Color, String line2, int line2Color) {
		this(null, title, titleColor, line1, line1Color, line2, line2Color);
	}
	
	public ListItem(Object metaData, String title, int titleColor, String line1, int line1Color, String line2, int line2Color) {
		this.title = title;
		this.line1 = line1;
		this.line2 = line2;
		this.titleColor = titleColor;
		this.line1Color = line1Color;
		this.line2Color = line2Color;
		this.metaData = metaData;
	}

	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof ListItem) {
			return ((ListItem)arg0).title.compareTo(this.title) * -1;
		} else {
			return 0;
		}
	}
}
