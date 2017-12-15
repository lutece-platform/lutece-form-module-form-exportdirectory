function displayHideIterationConfiguration(checkbox, idGroup) {
	var configName = 'group-' + idGroup;
	displayHideConfiguration(checkbox, configName);
}

function displayHideIterationConfigurationType(checkbox, idGroup) {
	var configName = 'type-group-' + idGroup;
	displayHideConfiguration(checkbox, configName);
	
	var className = 'button-iterable-group-' + idGroup;
	var buttonIterableGroupList = document.getElementsByClassName( className );
	if ( buttonIterableGroupList != undefined ) {
		for( var i = 0; i < buttonIterableGroupList.length; i++ ) {
			if ( checkbox.checked ) {
				buttonIterableGroupList[i].required = false;
			}
			else {
				buttonIterableGroupList[i].required = true;
			}
		}
	}
	
	var listTitleTypeGroup = document.getElementsByClassName("title-separator-group");
	if ( listTitleTypeGroup != undefined ) {
		if ( !checkbox.checked ) {
			for( var i = 0; i < listTitleTypeGroup.length; i++ ) {
				listTitleTypeGroup[i].style.display = "none";
			}
		}
		else {
			for( var i = 0; i < listTitleTypeGroup.length; i++ ) {
				listTitleTypeGroup[i].style.display = "";
			}
		}
	}
}

function displayHideConfiguration(checkbox, configName) {
	var listConfigurationGroup = document.getElementsByClassName(configName);
	if ( listConfigurationGroup != undefined ) {
		if ( checkbox.checked ) {
			for( var i = 0; i < listConfigurationGroup.length; i++ ) {
				listConfigurationGroup[i].style.display = "none";
			}
		}
		else {
			for( var i = 0; i < listConfigurationGroup.length; i++ ) {
				listConfigurationGroup[i].style.display = "";
			}
		}
	}
}