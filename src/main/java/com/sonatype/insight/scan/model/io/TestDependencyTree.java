/*
 * Copyright (c) 2011-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */

package com.sonatype.insight.scan.model.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sonatype.insight.scan.model.Dependency;
import com.sonatype.insight.scan.model.ProjectScanItem;

import com.thoughtworks.xstream.XStream;

public class TestDependencyTree
{
  private static final XStream xstream = XStreamFactory.newInstance();

  public static void main(String[] args) {
    ProjectScanItem projectItem = new ProjectScanItem("moduleKind", "moduleId");
    Map<String, List<String>> dependencies = new HashMap<String, List<String>>();
    List<String> dep1Deps = Arrays.asList("dep11", "dep12");
    List<String> dep2Deps = Arrays.asList("dep21", "dep22");
    List<String> dep22Deps = Arrays.asList("dep221", "dep222");
    dependencies.put("dep1", dep1Deps);
    dependencies.put("dep2", dep2Deps);
    dependencies.put("dep22", dep22Deps);

    for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
      String id = entry.getKey();
      Dependency dependency = new Dependency();
      dependency.setId(id);
      dependency.setDirect(true); //for now hardcoding, but we need to work this out properly
      List<String> childIds = entry.getValue();
      if (childIds != null && !childIds.isEmpty()) {
        for (String childId : childIds) {
          Dependency child = new Dependency();
          child.setId(childId);
          dependency.addDependency(child);
        }
      }
      projectItem.addDependency(dependency);
    }

    String xml = xstream.toXML(projectItem);
    System.out.println(xml);
    //produces below output
    /*
    <project id="moduleId" kind="moduleKind">
      <dependencies>
        <dep id="dep1" direct="true">
          <dep id="dep11"/>
          <dep id="dep12"/>
        </dep>
        <dep id="dep2" direct="true">
          <dep id="dep21"/>
          <dep id="dep22"/>
        </dep>
        <dep id="dep22" direct="true">
          <dep id="dep221"/>
          <dep id="dep222"/>
        </dep>
      </dependencies>
    </project>
     */
  }
}
