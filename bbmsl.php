<?php
/**
 * Plugin Name:  BBMSL Payment Gateway
 * Description:  Online payment solution for Hong Kong merchants. Supports Visa, Master, AMEX, Alipay, Wechat Pay, Apple Pay, Google Pay.
 * Author:       Coding Free Limited for BBMSL
 * Author URI:   https://www.bbmsl.com/
 * Version:      1.0.8
 * Requires PHP: 7.4
 * Text Domain:  bbmsl-gateway
 * Domain Path:  /i18n/languages/
 * License:      GNU General Public License v3.0
 * License URI:  http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @package   bbmsl-gateway
 * @author    Coding Free Limited for BBMSL
 * @category  Admin
 * @copyright Copyright( c) 2022 BBMSL Limited
 * @license   http://www.gnu.org/licenses/gpl-3.0.html GNU General Public License v3.0
 *
 */

namespace BBMSL;

defined( 'ABSPATH' ) || exit;

final class BBMSL{

	public const LOG_TYPE_WEBHOOK	= 'webhook';
	public const LOG_TYPE_DEBUG		= 'debug';
	
	public function __construct() {
		// define global constants
		if( ! defined( 'BBMSL_PLUGIN_FILE' ) ) {
			define( 'BBMSL_PLUGIN_FILE', __FILE__ );
		}
		if( ! defined( 'BBMSL_PLUGIN_DIR' ) ) {
			define( 'BBMSL_PLUGIN_DIR', __DIR__ . DIRECTORY_SEPARATOR );
		}

		// date_default_timezone_set( wp_timezone_string() );

		add_action( 'plugins_loaded', function() {
			include_once( BBMSL_PLUGIN_DIR . 'vendor' . DIRECTORY_SEPARATOR . 'autoload.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-bbmsl-sdk.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-bbmsl-setup.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-bbmsl.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-notice.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-utility.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-webhook.php' );
			include_once( BBMSL_PLUGIN_DIR . 'sdk' . DIRECTORY_SEPARATOR . 'class-wordpress.php' );
			Sdk\BBMSL::init();
			
			// create the logging directory, if not yet exist
			static::ensureLogDirectory();
			return true;
		} );

	}

	private static function getLoggingDirectory() {
		return BBMSL_PLUGIN_DIR . 'logs' . DIRECTORY_SEPARATOR;
	}

	private static function ensureLogDirectory() {
		$logging_dir = static::getLoggingDirectory();
		if( !is_dir( $logging_dir ) ) {
			mkdir( $logging_dir, 0755, true );
		}
		chmod( $logging_dir, 0755 );
		return realpath( $logging_dir );	
	}

	public static function putLog( string $log_type = '', $content = null, bool $stack = false ) {
		$type = 'debug';
		if( isset( $log_type ) && is_string( $log_type ) ) {
			$log_type = trim( $log_type );
			if( strlen( $log_type ) > 0 ) {
				$type = strtolower( $log_type );
			}
		}

		$log_directory = static::ensureLogDirectory();
		if( isset( $content ) ) {
			$line = array(
				'timestamp'	=> date( 'Y-m-d H:i:s.u' ),
				'type'		=> $type,
				'content'	=> $content,
			);
			
			if( $stack ) {
				$line[ 'stack' ] = debug_backtrace();
			}
			
			$log_file = rtrim( $log_directory, '\\/ ' ) . DIRECTORY_SEPARATOR . $type . '.log.json';
			if( !( file_exists( $log_file ) && !is_dir( $log_file ) ) ) {
				file_put_contents( $log_file, '[' . PHP_EOL );
			}
			file_put_contents( $log_file, json_encode( $line, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES ) . ',' . PHP_EOL, FILE_APPEND );
			return true;
		}
	}
}

new BBMSL();