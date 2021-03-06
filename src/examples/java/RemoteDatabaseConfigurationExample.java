/**
 *      Copyright (C) 2010 Lowereast Software
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mattinsler.guiceymongo.guice.GuiceyMongo;
import com.mattinsler.guiceymongo.guice.annotation.MongoDatabase;
import com.mongodb.DB;

public class RemoteDatabaseConfigurationExample {
	@Inject
	RemoteDatabaseConfigurationExample(@MongoDatabase(Databases.Main) DB mainDatabase, @MongoDatabase(Databases.Search) DB searchDatabase) {
		System.out.println(mainDatabase);
		System.out.println(searchDatabase);
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(
				// create a connection to localhost:13731 that can be used by databases
				GuiceyMongo.configureConnection(Connections.Primary)
					.port(13731),
					
				// create the production configuration
				GuiceyMongo.configure(Configurations.Production)
					// configure the Main database to use the Primary connection (localhost:13731)
					.mapDatabase(Databases.Main).to("prod_db").overConnection(Connections.Primary)
					.mapDatabase(Databases.Search).to("prod_search_db"),
					
				// create the test configuration
				GuiceyMongo.configure(Configurations.Test)
					// configure the Main database to use the Primary connection (localhost:13731)
					.mapDatabase(Databases.Main).to("test_db").overConnection(Connections.Primary),
				
				// configurations can be augmented in separate modules
				GuiceyMongo.configure(Configurations.Test)
					.mapDatabase(Databases.Search).to("test_search_db"),
				
				// choose the configuration to use
				GuiceyMongo.chooseConfiguration(Configurations.Test)
		);
		
		injector.getInstance(RemoteDatabaseConfigurationExample.class);
	}
}
