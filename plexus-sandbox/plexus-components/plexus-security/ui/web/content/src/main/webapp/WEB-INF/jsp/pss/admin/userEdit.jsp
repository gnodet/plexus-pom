<%--
  ~ Copyright 2005-2006 The Apache Software Foundation.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib prefix="pss" uri="plexusSecuritySystem"%>

<ww:form cssClass="edit" action="user" name="edit" method="post" namespace="/security/admin">
  <%@ include file="/WEB-INF/jsp/security/userCredentials.jspf"%>
  <ww:submit value="Update User" />
</ww:form>

<h2>Currently Assigned Roles</h2>

<ul>
  <ww:iterator id="role" value="edituser.assignedRoles">
    <li><em>${role}</em></li>
  </ww:iterator>
</ul>



<pss:ifAnyAuthorized permissions="grant-roles,remove-roles">
  <h2>Role Management</h2>

  <pss:ifAuthorized permission="grant-roles">
    <h3>Grant Role</h3>
    <ww:form action="user" name="grantRole" method="post" namespace="/security/admin">
      <ww:hidden name="principal" value="${edituser.username}" />
      <ww:hidden name="username" value="${edituser.username}" />
      <ww:select name="roleName" list="availableRoles" labelposition="top" />
      <ww:submit value="Grant" />
    </ww:form>
  </pss:ifAuthorized>

  <pss:ifAuthorized permission="remove-roles">
    <h3>Remove</h3>
    <ww:form action="user" name="removeRole" method="post" namespace="/security/admin">
      <ww:hidden name="principal" value="${edituser.username}" />
      <ww:hidden name="username" value="${edituser.username}" />
      <ww:select name="roleName" list="assignedRoles" labelposition="top" />
      <ww:submit value="Remove" />
    </ww:form>
  </pss:ifAuthorized>
</pss:ifAnyAuthorized>
