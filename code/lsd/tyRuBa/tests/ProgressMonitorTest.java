/* 
*    Ref-Finder
*    Copyright (C) <2015>  <PLSE_UCLA>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package tyRuBa.tests;

import tyRuBa.modes.TypeModeError;
import tyRuBa.parser.ParseException;
import tyRuBa.engine.ProgressMonitor;
import tyRuBa.engine.RuleBase;
import tyRuBa.engine.SimpleRuleBaseBucket;

public class ProgressMonitorTest extends TyrubaTest {

	SimpleRuleBaseBucket bucket;
	SimpleRuleBaseBucket otherBucket;

	public void setUp() throws Exception {
		super.setUp(mon);
		bucket = new SimpleRuleBaseBucket(frontend);
		otherBucket = new SimpleRuleBaseBucket(frontend);
	}

	MyProgressMonitor mon = new MyProgressMonitor();

	static class MyProgressMonitor implements ProgressMonitor {

		private boolean isDone = true;

		int updates = -99;
		int expectedWork;

		public void beginTask(String name, int totalWork) {
			updates = 0;
			expectedWork = totalWork;
			if (!isDone)
				fail("No multi tasking/progressing!");
			isDone = false;
			assertTrue(totalWork > 0);
		}

		public void worked(int units) {
			updates += units;
		}

		public void done() {
			isDone = true;
		}

		public int workDone() {
			if (!isDone)
				fail("Hey... the work is not done!");
			return updates;
		}

	};

	public ProgressMonitorTest(String arg0) {
		super(arg0);
	}

	public void testProgressMonitor() throws ParseException, TypeModeError {
		// This test assumes that buckets are autoUpdated.
		RuleBase.autoUpdate = true;

		frontend.parse("foo :: String");

		frontend.parse("foo(frontend).");
		otherBucket.addStuff("foo(otherBucket).");
		bucket.addStuff("foo(bucket).");

		test_must_succeed("foo(frontend)");

		assertEquals(mon.expectedWork, mon.workDone());

		test_must_succeed("foo(frontend)", otherBucket);

		otherBucket.clearStuff();

		test_must_succeed("foo(frontend)");

		assertEquals(mon.expectedWork, mon.workDone());

		mon.updates = -999;

		test_must_succeed("foo(frontend)", otherBucket);

		assertEquals(-999, mon.workDone());
		// monitor should not have been touched!

	}

}
