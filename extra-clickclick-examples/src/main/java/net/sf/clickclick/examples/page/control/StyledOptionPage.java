/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.examples.page.control;

import net.sf.clickclick.control.StyledOption;
import net.sf.clickclick.control.grid.Grid;
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Reset;
import org.apache.click.control.Select;

import java.util.ArrayList;
import java.util.List;

public class StyledOptionPage extends BorderPage {

    public Form      form              = new Form();
    private Grid     grid              = new Grid("grid");
    private Grid     grid2             = new Grid("grid2");
    private Reset    sbReset           = new Reset("Reset values");
    private Checkbox chJavascript      = new Checkbox("javascriptToggle");

    private Select   backgroundcolor   = new Select("a");
    private Select   backgroundcolor_w = new Select("w_b");
    private Select   color             = new Select("c");
    private Select   color_w           = new Select("w_d");
    private Select   utf8              = new Select("e");
    private Select   utf8_w            = new Select("w_f");
    private Select   firstLetter       = new Select("g");
    private Select   firstLetter_w     = new Select("w_h");
    private Select   images            = new Select("i");
    private Select   images_w          = new Select("w_j");
    private Select   font              = new Select("k");
    private Select   font_w            = new Select("w_l");
    private Select   fontSize          = new Select("m");
    private Select   fontSize_w        = new Select("w_n");

    private int      optionValue;

    public void onInit() {
        // -- safe
        backgroundcolor.addAll(optionCreator(new String[] { "aqua", "black", "blue", "fuchsia", "gray", "green", "lime", "maroon", "navy" }));
        backgroundcolor_w.addAll(optionCreator(new String[] { "aqua", "black", "blue", "fuchsia", "gray", "green", "lime", "maroon", "navy" }));
        color.addAll(optionCreator(new String[] { "aqua", "black", "blue", "fuchsia", "gray", "green", "lime", "maroon", "navy" }));
        color_w.addAll(optionCreator(new String[] { "aqua", "black", "blue", "fuchsia", "gray", "green", "lime", "maroon", "navy" }));
        utf8.addAll(optionCreator(new String[] { "\u2588\u2588 blue", "\u2588\u2588 fuchsia", "\u2588\u2588 gray", "\u2588\u2588 green", "\u2588\u2588 lime", "\u2640 Woman", "\u2642 Man",
                "\u265A King", "\u265B Queen", "\u265C Tower", "\u265D Rook", "\u265E Horse", "\u265F Pawn", "\u264E Libra", "\u263A Happy", "\u2601 Cloudy", "\u2602 Rainy", "\u2600 Sunny",
                "\u262F Yin/Yang", "\u266C Notes", "\u2713 Accept", "\u2715 Decline", "\u2660 Spades", "\u2665 Hearts", "\u2666 Diamonds", "\u2663 Clubs" }, new String[] { "blue", "fuchsia", "gray",
                "green", "lime", "Woman", "Man", "King", "Queen", "Tower", "Rook", "Horse", "Pawn", "Sad", "Happy", "Cloudy", "Rainy", "Sunny", "Yin-Yang", "Notes", "Accept", "Decline", "Spades",
                "Hearts", "Diamonds", "Clubs" }));
        utf8_w.addAll(optionCreator(new String[] { "\u2588\u2588 blue", "\u2588\u2588 fuchsia", "\u2588\u2588 gray", "\u2588\u2588 green", "\u2588\u2588 lime", "\u2640 Woman", "\u2642 Man",
                "\u265A King", "\u265B Queen", "\u265C Tower", "\u265D Rook", "\u265E Horse", "\u265F Pawn", "\u264E Libra", "\u263A Happy", "\u2601 Cloudy", "\u2602 Rainy", "\u2600 Sunny",
                "\u262F Yin/Yang", "\u266C Notes", "\u2713 Accept", "\u2715 Decline", "\u2660 Spades", "\u2665 Hearts", "\u2666 Diamonds", "\u2663 Clubs" }, new String[] { "blue", "fuchsia", "gray",
                "green", "lime", "Woman", "Man", "King", "Queen", "Tower", "Rook", "Horse", "Pawn", "Sad", "Happy", "Cloudy", "Rainy", "Sunny", "Yin-Yang", "Notes", "Accept", "Decline", "Spades",
                "Hearts", "Diamonds", "Clubs" }));
        firstLetter.addAll(optionCreator(new String[] { "\u2713 Accept", "\u2715 Decline", "\u2660 Spades", "\u2665 Hearts", "\u2666 Diamonds", "\u2663 Clubs" }, new String[] { "Accept", "Decline",
                "Spades", "Hearts", "Diamonds", "Clubs" }));
        firstLetter_w.addAll(optionCreator(new String[] { "\u2713 Accept", "\u2715 Decline", "\u2660 Spades", "\u2665 Hearts", "\u2666 Diamonds", "\u2663 Clubs" }, new String[] { "Accept", "Decline",
                "Spades", "Hearts", "Diamonds", "Clubs" }));
        images.addAll(optionCreator(new String[] { "Open", "Closed", "Gradient", "No color", "Calendar", "Folder", "Keyboard" }, new String[] { "open", "closed", "color", "nocolor", "calend",
                "folder", "keyb" }));
        images_w.addAll(optionCreator(new String[] { "Open", "Closed", "Gradient", "No color", "Calendar", "Folder", "Keyboard" }, new String[] { "open", "closed", "color", "nocolor", "calend",
                "folder", "keyb" }));
        font.addAll(optionCreator(new String[] { "Arial", "Arial Black", "Arial Narrow", "Book Antiqua", "Century Gothic", "Comic Sans MS", "Courier New", "Fixedsys", "Franklin Gothic Medium",
                "Garamond", "Georgia", "Impact", "Lucida Console", "Lucida Sans Unicode", "Sans Serif", "Palatino Linotype", "System", "Tahoma" }, new String[] { "Ari", "Arb", "Arn", "Boo", "Cen",
                "Com", "Cou", "Fix", "Fra", "Gar", "Geo", "Imp", "Luc", "Lus", "San", "Pal", "Sys", "Tah" }));
        font_w.addAll(optionCreator(new String[] { "Arial", "Arial Black", "Arial Narrow", "Book Antiqua", "Century Gothic", "Comic Sans MS", "Courier New", "Fixedsys", "Franklin Gothic Medium",
                "Garamond", "Georgia", "Impact", "Lucida Console", "Lucida Sans Unicode", "Sans Serif", "Palatino Linotype", "System", "Tahoma" }, new String[] { "Ari", "Arb", "Arn", "Boo", "Cen",
                "Com", "Cou", "Fix", "Fra", "Gar", "Geo", "Imp", "Luc", "Lus", "San", "Pal", "Sys", "Tah" }));
        fontSize.addAll(optionCreator(new String[] { "8 pt", "12 pt", "18 pt", "24 pt", "0.5 em", "1 em", "1.5 em", "2.0 em", "small", "medium", "large", "x-large" }, new String[] { "pt1", "pt2",
                "pt3", "pt4", "em1", "em2", "em3", "em4", "small", "medium", "large", "x-large" }));
        fontSize_w.addAll(optionCreator(new String[] { "8 pt", "12 pt", "18 pt", "24 pt", "0.5 em", "1 em", "1.5 em", "2.0 em", "small", "medium", "large", "x-large" }, new String[] { "pt1", "pt2",
                "pt3", "pt4", "em1", "em2", "em3", "em4", "small", "medium", "large", "x-large" }));

        backgroundcolor_w.setSize(6);
        color_w.setSize(6);
        utf8_w.setSize(6);
        firstLetter_w.setSize(6);
        images_w.setSize(6);
        font_w.setSize(6);
        fontSize_w.setSize(6);

        backgroundcolor.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");
        color.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");
        utf8.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");
        firstLetter.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");
        images.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");
        font.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");
        fontSize.setAttribute("onchange", "copyStyle(this.options[this.selectedIndex], this);");

        // -- grid & form
        grid.insert(new Label("<u>color</u>"), 1, 1);
        grid.insert(new Label("<u>background-color</u>"), 1, 2);
        grid.insert(new Label("<u>(UTF-8 Character)</u>"), 1, 3);
        grid.insert(new Label("<u>:first-letter</u>"), 1, 4);

        grid.insert(backgroundcolor, 2, 1);
        grid.insert(backgroundcolor_w, 3, 1);
        grid.insert(color, 2, 2);
        grid.insert(color_w, 3, 2);
        grid.insert(utf8, 2, 3);
        grid.insert(utf8_w, 3, 3);
        grid.insert(firstLetter, 2, 4);
        grid.insert(firstLetter_w, 3, 4);

        grid.insert(new Label("<i>safe</i>"), 4, 1);
        grid.insert(new Label("<i>safe</i>"), 4, 2);
        grid.insert(new Label("<i>safe, page must<br/>be UTF-8 encoded</i>"), 4, 3);
        grid.insert(new Label("<i>Firefox only<br/>(+ optionally UTF-8)<br/>dropdown javascript is flaky</i>"), 4, 4);

        grid2.insert(new Label("<u>background-image</u>"), 1, 1);
        grid2.insert(new Label("<u>font-family</u>"), 1, 2);
        grid2.insert(new Label("<u>font-size &amp; border</u>"), 1, 3);

        grid2.insert(images, 2, 1);
        grid2.insert(images_w, 3, 1);
        grid2.insert(font, 2, 2);
        grid2.insert(font_w, 3, 2);
        grid2.insert(fontSize, 2, 3);
        grid2.insert(fontSize_w, 3, 3);

        grid2.insert(new Label("<i>Firefox only</i>"), 4, 1);
        grid2.insert(new Label("<i>Firefox only<br/>depends on availability of<br/>fonts on client platform.</i>"), 4, 2);
        grid2.insert(new Label("<i>Firefox only<br />dropdown javascript: do not<br/>use 'em' but absolute values</i>"), 4, 3);

        chJavascript.setId("javascriptCheckbox");
        chJavascript.setAttribute("onchange", "toggleJavascript();");
        chJavascript.setChecked(true);

        form.add(new Label("<b>Compatibility and Sample Chart</b>"));
        form.add(grid);
        form.add(new Label("&nbsp;"));
        form.add(grid2);
        Span s = new Span();
        s.add(chJavascript);
        s.add(new HtmlLabel(chJavascript, "Turn Javascript on (Firefox only)"));
        form.add(s);
        form.add(sbReset);
    }

    // ---

    private List optionCreator(String[] names) {
        List opts = new ArrayList(names.length);
        for (int i = 0; i < names.length; i++) {
            opts.add(new StyledOption("" + (++optionValue), names[i]));
        }
        return opts;
    }

    private List optionCreator(String[] labels, String[] names) {
        if (names.length != labels.length) throw new IllegalArgumentException("not the same length");
        List opts = new ArrayList(names.length);
        for (int i = 0; i < names.length; i++) {
            opts.add(new StyledOption("" + (++optionValue), names[i], labels[i]));
        }
        return opts;
    }
}